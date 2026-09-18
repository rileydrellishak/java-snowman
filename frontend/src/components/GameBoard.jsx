import { getGameAPI, submitGuessAPI } from "../../api/utilities"
import { useRef, useState } from "react";
import Row from "./Row";
import GameStats from "./GameStats";

const GameBoard = ({ wordLength, currentGame, setCurrentGame }) => {
  const [letters, setLetters] = useState(Array(wordLength).fill(""))
  const [evals, setEvals] = useState([]);
  const [showRows, setShowRows] = useState(
    [Array(wordLength).fill(false)].map(
      (_, i) => {
        if (i == 0) {
          return true
        } return false
      }
    )
  )

  const inputRefs = useRef([])

  const handleLetterChange = (event, index) => {
    const newLetters = [...letters];
    newLetters[index] = event.target.value.toLowerCase();
    setLetters(newLetters);

    if (event.target.value && index < letters.length - 1) {
      inputRefs.current[index + 1].focus();
    }
  }

  const handleLetterKeyDown = (event, index) => {
    if (event.key === "Backspace" && !letters[index] && index > 0) {
      inputRefs.current[index - 1].focus();
    }
  }
  
  const handleSubmitGuess = async () => {
    const evaluation = await submitGuessAPI(letters.join(""));
    setEvals(prev => {
      const newEvals = [...prev];
      newEvals[currentGame.numGuesses] = evaluation;
      return newEvals;
    })
    
    const updatedGame = await getGameAPI();
    setCurrentGame(updatedGame);

    if (!updatedGame.winState) {
      setShowRows(prev => {
        const newRowsToShow = [...prev];
        newRowsToShow[updatedGame.numGuesses] = true;
        return newRowsToShow
      }
    )}
    setLetters(Array(wordLength).fill(""))
  }

  const allRows = Array.from({ length: currentGame.numGuesses + 1 }, (_, i) => i).map((i) => {
    if (i >= 5) {
      return null
    }
    return (
      <Row
        key={i}
        show={showRows[i]}
        inputRefs={inputRefs}
        handleLetterChange={handleLetterChange}
        handleLetterKeyDown={handleLetterKeyDown}
        wordLength={wordLength}
        evaluation={evals[i]}
      />
    )
  })

  return (
    <section className="game-screen">

      <main className="">

        <section className="guess-list">
          {allRows}
        </section>

        <button className="guess-submit" onClick={handleSubmitGuess}>Submit Guess</button>

      <GameStats currentGame={currentGame}/>
    
    </main>

    </section>
  )
}

export default GameBoard;