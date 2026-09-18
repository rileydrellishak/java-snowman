import { use, useState } from 'react'
import './App.css'
import SelectWordLength from './components/SelectWordLength'
import GameBoard from './components/GameBoard'

const App = () => {
  const [wordLength, setWordLength] = useState(null)
  const [gameStarted, setGameStarted] = useState(false)
  const [stats, setStats] = useState(null)
  const [currentGame, setCurrentGame] = useState(null)
  const [testMode, setTestMode] = useState(true)
  
  const resetButton = () => {
    setCurrentGame(null)
    setGameStarted(false)
    setWordLength(null)
  }

  const handleTestButton = () => {
    setTestMode(testMode => !testMode)
  }

  return (
    <section className="app">

      <header className='app-header'>
        <h1>Java Snowman</h1>
        
        <button
          className='start-button'
          type='reset'
          onClick={() => resetButton()}
        >reset</button>
        
        <div/>

        <button
          className='start-button'
          onClick={() => handleTestButton()}
        >
          test mode is {testMode == true ? 'true': 'false'}
        </button>
        
      </header>

      <main className='app-main'>
        
        {!gameStarted && <SelectWordLength
          wordLength={wordLength}
          setWordLength={setWordLength}
          setGameStarted={setGameStarted}
          setCurrentGame={setCurrentGame}
        />}
        {gameStarted && <GameBoard
          wordLength={wordLength}
          currentGame={currentGame}
          setCurrentGame={setCurrentGame}
        />}
      </main>
    </section>
  )
}

export default App
