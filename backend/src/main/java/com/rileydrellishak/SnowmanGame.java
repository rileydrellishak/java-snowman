package com.rileydrellishak;

import java.util.HashMap;
import java.util.Map;

public class SnowmanGame {
    private String[] hiddenWords = {"game", "house", "hidden", "differs"};

    private static final int MAX_NUM_GUESSES = 5;
    private static final int MIN_WORD_LENGTH = 4;
    private static final int MAX_WORD_LENGTH = 7;

    public String hiddenWord = "hidden";

    public int numGuesses = 0;
    public String[] guessedWords = new String[MAX_NUM_GUESSES];
    public boolean winState = false;
    public boolean stillPlaying = true;

    public void createNewGame(int wordLength) {
        if (wordLength > MAX_WORD_LENGTH || wordLength < MIN_WORD_LENGTH) {
            String errorString = "Word length must be between 4 and 7 letters.";
            throw new InvalidWordLengthException(errorString);
        } else {
            this.hiddenWord = this.hiddenWords[wordLength-4];
        }
    }

    public boolean checkWordLength(String word) {
        return word.length() == this.hiddenWord.length();
    }

    public int getHiddenWordLength() {
        return this.hiddenWord.length();
    }

    public boolean checkGuessedWord(String word) {
    int correctCount = 0;

    for (int i = 0; i < word.length(); i++) {
        char letter = word.charAt(i);
        char hiddenLetter = this.hiddenWord.charAt(i);

        if (letter == hiddenLetter) {
            correctCount++;
        }
    }

    this.winState = correctCount == this.hiddenWord.length();
    this.stillPlaying = !this.winState;

    return this.winState;
}

    private String canGuess() {
        if (!this.winState && this.numGuesses >= MAX_NUM_GUESSES) {
            return String.format(
                "Max num of guesses reached. The word was %s.",
                this.hiddenWord
            );
        } else if (this.winState) {
            return "Game already won";
        }
        
        return "";
    }
    
    public void submitGuess(String word) {
        if (!this.canGuess().isEmpty()) {
            throw this.winState
            ? new CompletedGameCannotAcceptAdditionalGuesses(this.canGuess())
            : new MaxNumGuessesReached(this.canGuess());
        }

        if (!checkWordLength(word)) {
            throw new InvalidWordLengthException(
                String.format("\"%s\" is too %s.", word,
                    word.length() > this.hiddenWord.length() ? "long" : "short")
            );
        } 
        
        this.guessedWords[numGuesses] = word;
        numGuesses += 1;
        this.checkGuessedWord(word);

        if (numGuesses == MAX_NUM_GUESSES) {
            this.stillPlaying = false;
        }
    }

    public Map<Integer, Character> mapWord(String word) {
        Map<Integer, Character> indexAndLetter = new HashMap<>();
        for (int i = 0; i < word.length(); i++) {
            indexAndLetter.put(i, word.charAt(i));
        } return indexAndLetter;
    }

    public Map<Integer, String> evaluateGuess(String word) {
        Map<Integer, Character> wordMap = mapWord(word);
        Map<Character, Integer> frequenciesInHidden = new HashMap<>();
        for (char ch : this.hiddenWord.toCharArray()) {
            frequenciesInHidden.merge(ch, 1, Integer::sum);
        }

        Map<Integer, String> wordMapResult = new HashMap<>();

        wordMap.forEach((index, letter) -> {
            if (letter == this.hiddenWord.charAt(index)) {
                wordMapResult.put(
                index,
                "correct"
            );
            frequenciesInHidden.merge(letter, -1, Integer::sum);
        }});

        wordMap.forEach((index, letter) -> {
            if (wordMapResult.containsKey(index)) {
                return;
            }

            if (frequenciesInHidden.getOrDefault(letter, 0) > 0) {
                wordMapResult.put(index, "present");
                frequenciesInHidden.merge(letter, -1, Integer::sum);
            } else {
                wordMapResult.put(index, "absent");
            }
        });

        return wordMapResult;
    }

}
