import { getGameAPI, startNewGameAPI } from "../../api/utilities";

const SelectWordLength = ({ wordLength, setWordLength, setGameStarted, setCurrentGame }) => {
  const buttons = [4, 5, 6, 7].map((i) => {
    return (
      <button
        key={i}
        onClick={() => setWordLength(i)}
        className={`word-length-button ${
          wordLength === i ? "selected" : ""
        }`}
      >
        {i}
      </button>
    )
  })

  const handleStartGame = async () => {
    startNewGameAPI(wordLength)
    const game = await getGameAPI()
    setCurrentGame(game);
    setGameStarted(true)
  }

  return (
    <section className="start-screen">
      <h2>choose a word length</h2>
      
      <section className="word-length-selection">
        {buttons}
      </section>

      <button
        className="start-button"
        onClick={handleStartGame}
      >
        Start Game
      </button>
    </section>
  )
};

export default SelectWordLength;