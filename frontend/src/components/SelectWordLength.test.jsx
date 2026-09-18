import { render, screen } from "@testing-library/react";
import "@testing-library/jest-dom/vitest";
import { describe, test, expect, vi, afterEach } from "vitest";
import { cleanup } from "@testing-library/react";
import axios from "axios"
import userEvent from "@testing-library/user-event";
import SelectWordLength from "./SelectWordLength";
import { kBaseURL } from "../../api/utilities";

vi.mock("axios")

afterEach(() => {
    cleanup();
});

// UI-001 The player can choose a word length before starting a game.
test("displays buttons for each word length", () => {
    render(<SelectWordLength />);

    for (let i = 4; i < 8; i++) {
      expect(
        screen.getByRole("button", { name: i.toString() })).toBeInTheDocument();
    }
  });

test("allows the player to choose a word length", async () => {
    const user = userEvent.setup();
    const setWordLength = vi.fn();

    render(
        <SelectWordLength
            wordLength={null}
            setWordLength={setWordLength}
        />
    );

    await user.click(screen.getByRole("button", { name: "5" }));

    expect(setWordLength).toHaveBeenCalledWith(5);
});

test("shows the selected word length", () => {
    const setWordLength = vi.fn();

    render(
        <SelectWordLength
            wordLength={5}
            setWordLength={setWordLength}
        />
    );

    expect(screen.getByRole("button", { name: "5" }))
        .toHaveClass("word-length-button selected");
});

// UI-002 The player can start a new game.
test("allows the player to start a game after selecting a word length", async () => {
  const user = userEvent.setup();
  const setWordLength = vi.fn();
  const setGameStarted = vi.fn();
  const setCurrentGame = vi.fn();

  axios.post.mockResolvedValue({
    data: {
      wordLength: 5,
    },
  });

  axios.get.mockResolvedValue({
    data: {
      guessedWords: [null, null, null, null, null],
      hiddenWord: "house",
      hiddenWordLength: 5,
      numGuesses: 0,
      stillPlaying: true,
      winState: false
    }
  })

  render(
    <SelectWordLength
      wordLength={5}
      setWordLength={setWordLength}
      setCurrentGame={setCurrentGame}
      setGameStarted={setGameStarted}
    />
  );

  await user.click(
    screen.getByRole("button", { name: "Start Game" })
  );

  expect(axios.post).toHaveBeenCalledWith(
    `${kBaseURL}/game/new?wordLength=5`
  );
  expect(axios.get).toHaveBeenCalledWith(
    `${kBaseURL}/game`
  )
});