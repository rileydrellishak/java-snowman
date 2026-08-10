package com.rileydrellishak;

public class CompletedGameCannotAcceptAdditionalGuesses extends RuntimeException{
    public CompletedGameCannotAcceptAdditionalGuesses(String message) {
        super(message);
    }
}
