# Development Phases

## 1: Java game engine

**Goal**: TDD to drive the idea of "given this game state + this input → I expect this result."

### Success Criteria

The Java game logic can:

- [ ] create a game for a requested word length
- [ ] select a hidden word of the requested length
- [ ] accept a guess
- [ ] reject guesses of the wrong length
- [ ] reject guesses that aren't valid words
- [ ] determine whether a guess is correct
- [ ] evaluate individual letters
- [ ] handle repeated letters correctly
- [ ] track guesses
- [ ] determine whether the game is won
- [ ] determine whether the game is lost
- [ ] prevent guesses after the game ends

### Requirements and Tests

1. A new game must use a word matching the requested length.
   - creating a 5-letter game produces a 5-letter target
   - creating a 6-letter game produces a 6-letter target
   - unsupported word lengths are rejected

2. A correct guess wins the game.
   - correct guess changes game status to WON
   - correct guess records the guess
   - additional guesses are rejected after winning

3. An incorrect guess consumes one attempt
   - initial guess count is zero
   - incorrect guess increments guess count
   - reaching maximum guesses changes status to LOST

4. Each letter receives the correct Wordle-style evaluation
   - Discovering edge cases and writing test cases for them
   - Example: when the word has duplicate letters

5. A game concludes when the correct word is guessed or the maximum number of guesses is reached.
   - Maximum number of guesses 5

## 2: Turn the engine into an API

**Goal**: The API should expose the behavior that already exists in the tested game engine.

### Success Criteria

The API can:

- [ ] start a new game
- [ ] return the game state
- [ ] submit a guess
- [ ] return the guess evaluation
- [ ] return the current game status
- [ ] reject invalid requests appropriately
- [ ] prevent interaction with completed games

### Requirements and Tests

API/integration tests around the existing logic:

- [ ] When a client starts a 5-letter game, the API returns a game with a 5-letter target.
- [ ] When a client submits a valid guess, the API returns the evaluation.
- [ ] When a client submits an invalid guess, the API returns an appropriate error.

Unit tests ask "Does my Java game logic work?"

Integration tests ask "Does my Spring application correctly expose that logic?"

## 3. Build the React game

**Goal**: Render the game and communicate with the API.

### Success criteria

- [ ] choose a word length
- [ ] start a game
- [ ] see the game board
- [ ] enter guesses
- [ ] submit guesses
- [ ] see letter evaluations
- [ ] see previous guesses
- [ ] see remaining attempts
- [ ] see the snowman state
- [ ] see a win screen
- [ ] see a loss screen
- [ ] start a new game

### Frontend tests

- [ ] Given a newly loaded game, the player sees the word-length selection.
- [ ] When the player selects 6 letters, the game starts with a 6-letter board.
- [ ] When a guess is submitted, the evaluated guess appears on the board.
- [ ] When the game is won, the win state is displayed.
- [ ] When the game is lost, the player is shown the answer.

## 4. Stats and localStorage

Only after the actual game works!

### Requirements

1. Define the stats before implementing them
   - Games played
   - Games won
   - Win percentage
   - Current streak
   - Best streak
   - Guess distribution

### Success criteria

- [ ] A completed game increments games played.
- [ ] A won game increments games won.
- [ ] Win percentage is calculated correctly.
- [ ] A win increases the current streak.
- [ ] A loss resets the current streak.
- [ ] Best streak is preserved.
- [ ] Guess distribution records the number of guesses used for wins.
- [ ] Stats survive page refresh.
- [ ] Starting a new game does not erase stats.
- [ ] Corrupted/missing localStorage data doesn't crash the app.

## 5. Polish/Snowman-ness

### Potential features to add

- snowman animation
- snowman pieces disappearing after incorrect guesses
- keyboard
- dark/light theme
- animations for letter reveals
- stats modal
- "play again"
- accessible keyboard navigation
- responsive mobile layout
- difficulty modes
- daily challenge