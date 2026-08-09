# Java Snowman

Revisiting and extending the Ada precourse project "Snowman" by using Java and adding back and front ends!

## Game Definition

Snowman is a Wordle-inspired word guessing game. Before starting, the player chooses a word length. The player then has a limited number of guesses to identify the hidden word. Each incorrect guess causes the snowman to lose a piece.

## Development

### Backend

From the project root:

```bash
cd backend
./mvnw test
```

To run the Java application:

```bash
./mvnw exec:java
```

### Frontend

From the project root:

```bash
cd frontend
npm run dev
```

The frontend will be available at the local URL Vite prints in the terminal.
