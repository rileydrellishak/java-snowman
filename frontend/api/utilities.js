import axios from 'axios'

const kBaseURL = "http://localhost:8080"

// /game/new?wordLength=num to create a game

const startNewGameAPI = (wordLength) => {
  return axios.post(`${kBaseURL}/game/new?wordLength=${wordLength}`)
  .then(response => response.data)
  .catch(error => console.log(error))
}

const getGameAPI = () => {
  return axios.get(`${kBaseURL}/game`)
  .then(response => response.data)
  .catch(error => console.log(error))
}

const submitGuessAPI = (guess) => {
  return axios.post(
    `${kBaseURL}/game/guess`, 
    guess,
    {
      headers: {
        "Content-Type": "text/plain"
      }
    }
  )
  .then(response => response.data)
  .catch(error => console.log(error))
}

export { kBaseURL, startNewGameAPI, getGameAPI, submitGuessAPI };