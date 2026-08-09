# Core Rules and Assumptions

- Player chooses a word length before starting.
- The word must match the selected length.
- Player has a limited number of guesses.
- Each guess must be a valid word of the correct length.
- A guess evaluates each letter against the hidden word.
- Letters can be:
  - correct letter + correct position
  - correct letter + incorrect position
  - not present
- The game ends when:
  - the player guesses the word, or
  - the player runs out of guesses.
- A completed game records whether the player won/lost and how many guesses they used.
- Stats persist in the browser.