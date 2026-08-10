# Snowman — Requirements

Snowman is a Wordle-inspired word guessing game. Before starting a game, the player chooses the length of the hidden word. The player then has a limited number of guesses to identify the word before the snowman melts.

## Game Rules

* The player selects a supported word length before starting a game.
* The game selects a hidden word matching the selected length.
* The player has a limited number of guesses.
* Each guess must be a valid word matching the hidden word's length.
* Each letter in a guess is evaluated as:

  * **Correct** — the letter is in the correct position.
  * **Present** — the letter appears in the hidden word but is in the wrong position.
  * **Absent** — the letter does not appear in the hidden word.
* The game ends when:

  * The player correctly guesses the hidden word, or
  * The player uses all available guesses.
* A completed game records whether the player won or lost and, if applicable, how many guesses were used.
* Player statistics are stored in the browser and persist between sessions.

## Requirements

### Game Logic

| ID       | Requirement                                                                                      | Phase | Tested? |
| -------- | ------------------------------------------------------------------------------------------------ | ----: | :-----: |
| GAME-001 | A player can create a new game with a supported word length.                                     |     1 |     ✅  |
| GAME-002 | A new game's hidden word matches the requested word length.                                      |     1 |    ✅   |
| GAME-003 | Unsupported word lengths are rejected.                                                           |     1 |    ✅   |
| GAME-004 | A new game begins with zero guesses.                                                             |     1 |    ✅   |
| GAME-005 | A player can submit a guess.                                                                     |     1 |    ✅   |
| GAME-006 | A guess must match the hidden word's length.                                                     |     1 |    ✅   |
| GAME-007 | A guess must be a valid word.                                                                    |     1 |    ☐    |
| GAME-008 | A submitted guess is recorded by the game.                                                       |     1 |    ✅   |
| GAME-009 | Each letter in a guess is evaluated as correct, present, or absent.                              |     1 |    ✅   |
| GAME-010 | Repeated letters are evaluated correctly when the hidden word contains repeated letters.         |     1 |    ☐    |
| GAME-011 | A correct guess changes the game status to won.                                                  |     1 |    ✅   |
| GAME-012 | An incorrect guess consumes one available attempt.                                               |     1 |    ✅   |
| GAME-013 | The game changes to lost when the player uses all available attempts without guessing correctly. |     1 |    ☐    |
| GAME-014 | A completed game cannot accept additional guesses.                                               |     1 |    ✅   |
| GAME-015 | The game can provide its current status.                                                         |     1 |    ✅   |
| GAME-016 | The game can provide the guesses submitted so far.                                               |     1 |   ✅    |

### API

| ID      | Requirement                                                       | Phase | Tested? |
| ------- | ----------------------------------------------------------------- | ----: | :-----: |
| API-001 | A client can request a new game with a selected word length.      |     2 |    ☐    |
| API-002 | The API returns the current game state when a game is created.    |     2 |    ☐    |
| API-003 | A client can submit a guess for an active game.                   |     2 |    ☐    |
| API-004 | The API returns the evaluation of a submitted guess.              |     2 |    ☐    |
| API-005 | The API returns the current game status.                          |     2 |    ☐    |
| API-006 | The API rejects guesses that do not meet the game's requirements. |     2 |    ☐    |
| API-007 | The API prevents guesses after a game has ended.                  |     2 |    ☐    |
| API-008 | The API returns appropriate errors for invalid requests.          |     2 |    ☐    |

### Frontend

| ID     | Requirement                                                                                  | Phase | Tested? |
| ------ | -------------------------------------------------------------------------------------------- | ----: | :-----: |
| UI-001 | The player can choose a word length before starting a game.                                  |     3 |    ☐    |
| UI-002 | The player can start a new game.                                                             |     3 |    ☐    |
| UI-003 | The game board displays the correct number of letter positions for the selected word length. |     3 |    ☐    |
| UI-004 | The player can enter a guess.                                                                |     3 |    ☐    |
| UI-005 | The player can submit a guess.                                                               |     3 |    ☐    |
| UI-006 | Submitted guesses are displayed on the game board.                                           |     3 |    ☐    |
| UI-007 | Letter evaluations are visually distinguishable as correct, present, or absent.              |     3 |    ☐    |
| UI-008 | The player can see their previous guesses.                                                   |     3 |    ☐    |
| UI-009 | The player can see how many attempts remain.                                                 |     3 |    ☐    |
| UI-010 | The snowman visually reflects the player's remaining attempts.                               |     3 |    ☐    |
| UI-011 | The player sees a win state when they correctly guess the word.                              |     3 |    ☐    |
| UI-012 | The player sees a loss state when they run out of attempts.                                  |     3 |    ☐    |
| UI-013 | The player can start a new game after completing a game.                                     |     3 |    ☐    |
| UI-014 | The player can see the hidden word when a game is lost.                                      |     3 |    ☐    |

### Statistics & Persistence

| ID       | Requirement                                                                       | Phase | Tested? |
| -------- | --------------------------------------------------------------------------------- | ----: | :-----: |
| STAT-001 | A completed game is recorded in the player's statistics.                          |     4 |    ☐    |
| STAT-002 | Games played is incremented when a game ends.                                     |     4 |    ☐    |
| STAT-003 | Games won is incremented when a player wins.                                      |     4 |    ☐    |
| STAT-004 | Win percentage is calculated from completed games.                                |     4 |    ☐    |
| STAT-005 | A winning game increases the player's current win streak.                         |     4 |    ☐    |
| STAT-006 | A losing game resets the player's current win streak.                             |     4 |    ☐    |
| STAT-007 | The player's best win streak is preserved.                                        |     4 |    ☐    |
| STAT-008 | A winning game is recorded in the guess distribution.                             |     4 |    ☐    |
| STAT-009 | Statistics persist after the browser is refreshed.                                |     4 |    ☐    |
| STAT-010 | Starting a new game does not erase existing statistics.                           |     4 |    ☐    |
| STAT-011 | Missing or invalid stored statistics do not prevent the application from loading. |     4 |    ☐    |
