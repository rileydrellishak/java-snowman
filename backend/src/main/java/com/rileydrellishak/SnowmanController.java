package com.rileydrellishak;

import org.springframework.http.ResponseEntity;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SnowmanController {
    private SnowmanGame currentGame;

    void resetGame() {
        currentGame = null;
    }

    @GetMapping("/hello")
    public String hello(
        @RequestParam(value = "name", defaultValue = "Snowman") String name) {
        return String.format("Hello %s!", name);
    };

    @ExceptionHandler(InvalidWordLengthException.class)
    public ResponseEntity<Void> handleInvalidWordLengthException(InvalidWordLengthException e) {
        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler(NumberFormatException.class)
    public ResponseEntity<Void> handleNumberFormatException(
        NumberFormatException e
    ) {
        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler(MaxNumGuessesReached.class)
    public ResponseEntity<Void> handleMaxNumGuessesReached(MaxNumGuessesReached e) {
        return ResponseEntity.badRequest().build();
    }

    @PostMapping("/game/new")
    public ResponseEntity<SnowmanGame> newGame(
        @RequestParam(value = "wordLength", defaultValue = "4") String wordLength
    ) {
        SnowmanGame game = new SnowmanGame(Integer.parseInt(wordLength));
        this.currentGame = game;
        return new ResponseEntity<>(game, HttpStatus.CREATED);
    } 

    @GetMapping("/game")
    public ResponseEntity<SnowmanGame> getGame() {
        if (this.currentGame == null) {
            return ResponseEntity.notFound().build();
        }
        return new ResponseEntity<>(this.currentGame, HttpStatus.OK);
    }

    @PostMapping("/game/guess")
    public ResponseEntity<Map<Integer, String>> makeGuess(
            @RequestBody String guess
    ) {
        if (this.currentGame == null) {
            return ResponseEntity.notFound().build();
        } else if (this.currentGame.winState) {
            return ResponseEntity.badRequest().build();
        }

        Map<Integer, String> evaluation = this.currentGame.submitGuess(guess);

        return ResponseEntity.ok(evaluation);
    }
}