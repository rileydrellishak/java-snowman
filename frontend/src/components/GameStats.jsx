const GameStats = ({ currentGame }) => {
  return (
    <section className="">
      <h2>Game Stats</h2>
      {
        !currentGame.winState && 
        currentGame.stillPlaying &&
        <h3>{5 - currentGame.numGuesses} guesses left</h3>
      }

      {currentGame.winState && <h3>you win!</h3>}

      {
        !currentGame.winState &&
        !currentGame.stillPlaying &&
        <>
        <h3>you lost</h3>
        <p>the word was {currentGame.hiddenWord}</p>
        </>
      }
    </section>
  )
}

export default GameStats;