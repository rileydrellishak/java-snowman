import { render, screen } from "@testing-library/react";
import "@testing-library/jest-dom/vitest";
import { describe, test, expect, vi, afterEach } from "vitest";
import { cleanup } from "@testing-library/react";
import axios from "axios"
import userEvent from "@testing-library/user-event";
import GameBoard from "./GameBoard";
import { kBaseURL } from "../../api/utilities";

vi.mock("axios")

afterEach(() => {
    cleanup();
});

const start4LetterWordGame2GuessesAndWin = async () => {
  const user = userEvent.setup();
  const setCurrentGame = vi.fn();
  const gameBefore = {
    guessedWords: [null, null, null, null],
    hiddenWord: "game",
    hiddenWordLength: 4,
    numGuesses: 0,
    stillPlaying: true,
    winState: false
  }

  const gameEnd = {
    guessedWords: ["germ", "game", null, null],
    hiddenWord: "game",
    hiddenWordLength: 4,
    numGuesses: 0,
    stillPlaying: true,
    winState: false
  }

  const guess1 = ["g", "e", "r", "m"];
  const guess2 = ["g", "a", "m", "e"];

  render(
  <GameBoard
    wordLength={4}
    currentGame={gameEnd}
    setCurrentGame={setCurrentGame}
  />);

  const evaluation = {
      0: "correct",
      1: "correct",
      2: "correct",
      3: "correct"
  }

}

const start4LetterWordGameAnd1Guess = async () => {
  const user = userEvent.setup();
  const setCurrentGame = vi.fn();
  const gameBefore = {
    guessedWords: [null, null, null, null],
    hiddenWord: "game",
    hiddenWordLength: 4,
    numGuesses: 0,
    stillPlaying: true,
    winState: false
  }

  const gameAfter = {
      guessedWords: ["abcde", null, null, null, null],
      hiddenWord: "house",
      hiddenWordLength: 5,
      numGuesses: 1,
      stillPlaying: true,
      winState: false
    }

  const evaluation = {
      0: "present",
      1: "correct",
      2: "absent",
      3: "correct"
  }
  axios.post.mockResolvedValue({data: evaluation})
  axios.get.mockResolvedValue({data: gameAfter})

  const guess = ['m', 'a', 'n', 'e']

  render(
  <GameBoard
    wordLength={4}
    currentGame={gameBefore}
    setCurrentGame={setCurrentGame}
  />);

    
  const inputs = screen.getAllByRole("textbox");

  for (let i = 0; i < gameBefore.hiddenWordLength; i++) {
    await user.type(inputs[i], guess[i])
    expect(inputs[i]).toHaveValue(guess[i])
  }

  await user.click(
    screen.getByRole("button", { name: "Submit Guess" })
  )

  await expect(axios.post).toHaveBeenCalledWith(
    `${kBaseURL}/game/guess`, guess.join(""), {headers: {"Content-Type": "text/plain"}}
  )
}

// UI-003 The game board displays the correct number of letter positions for the selected word length.
test("displays correct number of letter positions for the selected word length", () => {
  const game = {
    guessedWords: [null, null, null, null, null],
    hiddenWord: "house",
    hiddenWordLength: 5,
    numGuesses: 0,
    stillPlaying: true,
    winState: false
  }

  render(
  <GameBoard
    wordLength={5}
    currentGame={game}
  />);

  expect(screen.getAllByRole("textbox")).toHaveLength(5);    
});

// UI-004 The player can enter a guess.
test("player can type one letter into each input tile", async () => {
  const user = userEvent.setup();

  const game = {
    guessedWords: [null, null, null, null, null],
    hiddenWord: "house",
    hiddenWordLength: 5,
    numGuesses: 0,
    stillPlaying: true,
    winState: false
  }

  const guess = ["a", "b", "c", "d", "e"];
  
  render(
  <GameBoard
    wordLength={5}
    currentGame={game}
  />);

  const inputs = screen.getAllByRole("textbox");

  for (let i = 0; i < game.hiddenWordLength; i++) {
    await user.type(inputs[i], guess[i])
    expect(inputs[i]).toHaveValue(guess[i])
  }

})

// UI-005 The player can submit a guess.
// UI-006 Submitted guesses are displayed on the game board.
// UI-008 The player can see their previous guesses.
test("player can submit a guess when each tile has one letter in it", async () => {
  const user = userEvent.setup();
  const setCurrentGame = vi.fn();
  
  axios.post.mockResolvedValue({
    data: {
      0: "absent",
      1: "absent",
      2: "absent",
      3: "absent",
      4: "correct"
    }
  })

  axios.get.mockResolvedValue({
    data: {
      guessedWords: ["abcde", null, null, null, null],
      hiddenWord: "house",
      hiddenWordLength: 5,
      numGuesses: 1,
      stillPlaying: true,
      winState: false
    }
  })

  const game = {
    guessedWords: [null, null, null, null, null],
    hiddenWord: "house",
    hiddenWordLength: 5,
    numGuesses: 0,
    stillPlaying: true,
    winState: false
  }

  const guess = ["a", "b", "c", "d", "e"];
  
  render(
  <GameBoard
    wordLength={5}
    currentGame={game}
    setCurrentGame={setCurrentGame}
  />);
  
  const inputs = screen.getAllByRole("textbox");

  for (let i = 0; i < game.hiddenWordLength; i++) {
    await user.type(inputs[i], guess[i])
    expect(inputs[i]).toHaveValue(guess[i])
  }

  await user.click(
    screen.getByRole("button", { name: "Submit Guess" })
  )

  expect(axios.post).toHaveBeenCalledWith(
    `${kBaseURL}/game/guess`, guess.join(""), {headers: {"Content-Type": "text/plain"}}
  )

  expect(axios.get).toHaveBeenCalledWith(`${kBaseURL}/game`)

  for (let i = 0; i < game.hiddenWordLength; i++) {
    expect(inputs[i]).toHaveValue(guess[i])
  }

});

// UI-007 Letter evaluations are visually distinguishable as correct, present, or absent.
test("player can see that the letters are visually evaluated - green for correct, yellow for present, gray for absent", async () => {
  const evaluation = {
      0: "present",
      1: "correct",
      2: "absent",
      3: "correct"
  }
  
  await start4LetterWordGameAnd1Guess();

  const inputs = screen.getAllByRole("textbox");

  for (let i = 0; i < 4; i++) {
    expect(inputs[i]).toHaveClass(evaluation[i])
  }
})

// UI-009 The player can see how many attempts remain.
test("player can see how many guesses they have left", async () => {
  await start4LetterWordGameAnd1Guess();
  // should have 4 guesses left
  expect(screen.getByText('guesses left', { exact: false })).toBeVisible();
})

//UI-011 The player can see a win state when they correctly guess the word.
test("player can see the win state when they guess word correctly", async () => {
  const setCurrentGame = vi.fn();

  const gameEnd = {
    guessedWords: ["germ", "game", null, null],
    hiddenWord: "game",
    hiddenWordLength: 4,
    numGuesses: 2,
    stillPlaying: false,
    winState: true
  }

  render(
  <GameBoard
    wordLength={4}
    currentGame={gameEnd}
    setCurrentGame={setCurrentGame}
  />);

  expect(screen.getByText('win', { exact: false })).toBeVisible();
})

// UI-012 The player sees a loss state when they run out of attempts.
test("player can see loss state when they run out of guesses", async () => {
  const setCurrentGame = vi.fn();

  const gameEnd = {
    guessedWords: ["germ", "worm", "derm", "mane", "mops"],
    hiddenWord: "game",
    hiddenWordLength: 4,
    numGuesses: 5,
    stillPlaying: false,
    winState: false
  }

  render(
  <GameBoard
    wordLength={4}
    currentGame={gameEnd}
    setCurrentGame={setCurrentGame}
  />);

  expect(screen.getByText('lost', { exact: false })).toBeVisible();
  expect(screen.getByText('the word was game')).toBeVisible();
})