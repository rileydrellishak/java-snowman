package com.rileydrellishak;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

public class SnowmanGameTest {
    // Counting the number of guesses
    @Test
    void newGameHasZeroGuesses() {
        int numGuesses = 0;
        SnowmanGame testGame = new SnowmanGame(4);
        assertEquals(numGuesses, testGame.numGuesses);
        for (String word: testGame.guessedWords) {
            assertNull(word);
        }
    }

    @Test
    void gameShowsNumOfGuessesSubmittedSoFar() {
        SnowmanGame testGame = new SnowmanGame(4);
        testGame.hiddenWord = "game";
        String[] words = {"home", "cool", "fool", "stay", "make"};
        for (int i = 0; i < words.length; i++) {
            testGame.submitGuess(words[i]);
            assertEquals(i+1, testGame.numGuesses);
        }
    }

    // Word length validation
    @Test
    void rejectsWordsThatAreInvaidLengths() {
        String[] words = {"the", "abcdefghijklm"};
        SnowmanGame testGame = new SnowmanGame(4);

        for (String word : words) {
            assertFalse(testGame.checkWordLength(word));
        }
    }

    @Test
    void acceptsWordsThatAreValidLengths() {
        SnowmanGame testGame = new SnowmanGame(5);
        testGame.hiddenWord = "guess";
        String[] words = {"guess", "messy", "house", "horse"};
        
        for (String word : words) {
            assertTrue(testGame.checkWordLength(word));
        }
    }

    // Submit guesses and persist guesses
    @Test
    void playerSubmitsWordFirstGuess() {
        SnowmanGame testGame = new SnowmanGame(5);

        testGame.hiddenWord = "guess";
        String guess = "guess";

        testGame.submitGuess(guess);

        assertEquals(guess, testGame.guessedWords[0]);
        assertEquals(1, testGame.numGuesses);
        for (int i = 1; i < 5; i ++) {
            assertNull(testGame.guessedWords[i]);
        }
    }

    @Test
    void playSubmitsWordMultipleGuesses() {
        String[] guesses = {"guess", "messy", "hello", null, null};
        SnowmanGame testGame = new SnowmanGame(5);

        testGame.hiddenWord = "words";
        for (int i = 0; i < testGame.getHiddenWordLength(); i++) {
            if (guesses[i] == null) {
                assertNull(testGame.guessedWords[i]);
            } else {
                testGame.submitGuess(guesses[i]);
                assertEquals((i+1), testGame.numGuesses);
                assertNotNull(testGame.guessedWords[i]);
            }
        }
    }

    // Ensures invalid guesses are not persisted
    @Test
    void playerSubmitsInvalidWord() {
        SnowmanGame testGame = new SnowmanGame(4);

        String[] invalidWords = {"the", "abcdefghijklm"};

        for (String word: invalidWords) {
            String errorString = String.format(
                "\"%s\" is too %s.",
                word,
                word.length() > testGame.getHiddenWordLength()
                ? "long"
                : "short"
            );

            InvalidWordLengthException exception = assertThrows(
                InvalidWordLengthException.class, () -> {
                    testGame.submitGuess(word);
                }
            );
            assertEquals(errorString, exception.getMessage());
            assertFalse(Arrays.asList(testGame.guessedWords).contains(word));
        }
    } 

    // Confirms guessing the correct word turns the winState to true
    @Test
    void playerGuessesCorrectWord() {
        SnowmanGame testGame = new SnowmanGame(4);
        testGame.hiddenWord = "guess";
        testGame.submitGuess("guess");

        assertTrue(testGame.winState);
    }

    // Confirming mapping indices to characters
    // word = {0: w, 1: o, 2: r, 3: d}
    
    @Test
    void mapWordMapsIndicesToChars() {
        SnowmanGame testGame = new SnowmanGame(4);

        String example = "word";
        Map<Integer, Character> expectedMap = new HashMap<>();
        expectedMap.put(0, 'w');
        expectedMap.put(1, 'o');
        expectedMap.put(2, 'r');
        expectedMap.put(3, 'd');

        assertEquals(expectedMap, testGame.mapWord(example));
    }

    // Creating a new game
    @Test
    void createNewGameWithValidWordLength() {
        Integer wordLength = 5;
        SnowmanGame testGame = new SnowmanGame(wordLength);
        assertEquals(wordLength, testGame.getHiddenWordLength());
    }

    @Test
    void createNewGameWithInvalidWordLength() {
        Integer[] invalidWordLengths = {2, 20};
        for (Integer i: invalidWordLengths) {
            String errorString = "Word length must be between 4 and 7 letters.";

            InvalidWordLengthException exception = assertThrows(
                InvalidWordLengthException.class, () -> {
                    new SnowmanGame(i);;
                }
            );
            assertEquals(errorString, exception.getMessage());
        }
    }

    // Comparing the guessed word to the hidden word
    @Test
    void evaluateGuessAllAbsent() {
        SnowmanGame testGame = new SnowmanGame(5);

        testGame.hiddenWord = "horse";
        String guess = "quick";
        Map<Integer, String> expected = new HashMap<>();
        expected.put(0, "absent");
        expected.put(1, "absent");
        expected.put(2, "absent");
        expected.put(3, "absent");
        expected.put(4, "absent");

        assertEquals(expected, testGame.evaluateGuess(guess));
    }

    @Test
    void evaluateGuessAllCorrect() {
        SnowmanGame testGame = new SnowmanGame(5);

        testGame.hiddenWord = "horse";
        String guess = "horse";
        Map<Integer, String> expected = new HashMap<>();
        expected.put(0, "correct");
        expected.put(1, "correct");
        expected.put(2, "correct");
        expected.put(3, "correct");
        expected.put(4, "correct");

        assertEquals(expected, testGame.evaluateGuess(guess));
    }

    @Test
    void evaluateGuessAllPresent() {
        SnowmanGame testGame = new SnowmanGame(5);

        testGame.hiddenWord = "abcde";
        String guess = "eabcd";
        Map<Integer, String> expected = new HashMap<>();
        expected.put(0, "present");
        expected.put(1, "present");
        expected.put(2, "present");
        expected.put(3, "present");
        expected.put(4, "present");

        assertEquals(expected, testGame.evaluateGuess(guess));
    }

    @Test
    void evaluateGuessAllThreeStatuses() {
        SnowmanGame testGame = new SnowmanGame(5);

        testGame.hiddenWord = "horse";
        String guess = "shone";
        Map<Integer, String> expected = new HashMap<>();
        expected.put(0, "present");
        expected.put(1, "present");
        expected.put(2, "present");
        expected.put(3, "absent");
        expected.put(4, "correct");

        assertEquals(expected, testGame.evaluateGuess(guess));
    }

    @Test
    void evaluateGuessWithDuplicateLetters() {
        SnowmanGame testGame = new SnowmanGame(5);

        testGame.hiddenWord = "valley";
        String guess = "alleys";
        Map<Integer, String> expected = new HashMap<>();
        expected.put(0, "present");
        expected.put(1, "present");
        expected.put(2, "correct");
        expected.put(3, "present");
        expected.put(4, "present");
        expected.put(5, "absent");

        assertEquals(expected, testGame.evaluateGuess(guess));
    }

    @Test
    void evaluateGuessDoesNotOvercountDuplicateLetters() {
        SnowmanGame testGame = new SnowmanGame(5);

        testGame.hiddenWord = "valley";
        String guess = "llllll";

        Map<Integer, String> expected = new HashMap<>();
        expected.put(0, "absent");
        expected.put(1, "absent");
        expected.put(2, "correct");
        expected.put(3, "correct");
        expected.put(4, "absent");
        expected.put(5, "absent");

        assertEquals(expected, testGame.evaluateGuess(guess));
    }

    @Test
    void evaluateGuessWhenHiddenHasDuplicates() {
        SnowmanGame testGame = new SnowmanGame(4);

        testGame.hiddenWord = "boom";
        String guess = "boss";
        Map<Integer, String> expected = new HashMap<>();
        expected.put(0, "correct");
        expected.put(1, "correct");
        expected.put(2, "absent");
        expected.put(3, "absent");

        assertEquals(expected, testGame.evaluateGuess(guess));
    }


    // Validating that guessing the correct word results in a win
    @Test
    void guessCorrectWordResultsInWinStatus() {
        SnowmanGame testGame = new SnowmanGame(4);

        testGame.hiddenWord = "game";
        String[] words = {"home", "cool", "game"};
        for (String word: words) {
            testGame.submitGuess(word);
        }

        assertTrue(testGame.winState);
        assertFalse(testGame.stillPlaying);
        assertEquals(3, testGame.numGuesses);
    }

    // Scenarios that use all the guesses
    @Test
    void useAllGuessesAndLoseGame() {
        SnowmanGame testGame = new SnowmanGame(4);

        testGame.hiddenWord = "game";
        String[] words = {"home", "cool", "fool", "dome", "shed"};
        for (String word: words) {
            testGame.submitGuess(word);
        }

        assertFalse(testGame.winState);
        assertFalse(testGame.stillPlaying);
    }

    @Test
    void exceedingNumOfGuessesThrowsMaxNumGuessesError() {
        SnowmanGame testGame = new SnowmanGame(4);

        testGame.hiddenWord = "game";
        String[] words = {"home", "cool", "fool", "dome", "shed"};
        for (String word: words) {
            testGame.submitGuess(word);
        }

        assertFalse(testGame.winState);
        assertFalse(testGame.stillPlaying);

        String errorString = String.format(
            "Max num of guesses reached. The word was %s.",
            testGame.hiddenWord
        );

        MaxNumGuessesReached exception = assertThrows(
            MaxNumGuessesReached.class, () -> {
                testGame.submitGuess("eeee");
            }
        );
        assertEquals(errorString, exception.getMessage());
    }

    @Test
    void completedGameCannotAcceptAdditionalGuesses() {
        SnowmanGame testGame = new SnowmanGame(4);

        testGame.hiddenWord = "game";
        String[] words = {"home", "cool", "game"};
        for (String word: words) {
            testGame.submitGuess(word);
        } 

        assertTrue(testGame.winState);
        assertFalse(testGame.stillPlaying);

        String errorString = "Game already won";
        CompletedGameCannotAcceptAdditionalGuesses exception = assertThrows(
            CompletedGameCannotAcceptAdditionalGuesses.class, () -> {
                testGame.submitGuess("dome");
            }
        ); assertEquals(errorString, exception.getMessage());
        assertFalse(Arrays.asList(testGame.guessedWords).contains("dome"));
    }

    @Test
    void gameProvidesStatus() {
        SnowmanGame testGame = new SnowmanGame(4);

        testGame.hiddenWord = "game";
        String[] words = {"home", "cool"};
        for (String word: words) {
            testGame.submitGuess(word);
        }
        assertTrue(testGame.stillPlaying);
        assertFalse(testGame.winState);
    }
}
