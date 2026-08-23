package com.rileydrellishak;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import static org.hamcrest.Matchers.hasLength;
import static org.hamcrest.Matchers.hasItem;

import java.util.HashMap;
import java.util.Map;
import java.util.List;

import javax.print.attribute.standard.Media;

@WebMvcTest(SnowmanController.class)
public class SnowmanControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    SnowmanController snowmanController;

    @BeforeEach
    void setUp() {
        snowmanController.resetGame();
    }

    // API-001 A client can request a new game with a selected word length.
    // API-002 The API returns the current game state when a game is created.

    @Test void startNewGame() throws Exception {
        Integer[] validWordLengths = {4, 5, 6, 7};
        for (Integer i: validWordLengths) {
            mockMvc.perform(
                post("/game/new")
                    .param("wordLength", i.toString())
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.numGuesses").value(0))
                .andExpect(jsonPath("$.hiddenWord").isNotEmpty())
                .andExpect(jsonPath("$.hiddenWord").value(hasLength(i)))
                .andExpect(jsonPath("$.winState").value(false));     
            }
        }

    @Test void startNewGameInvalidWordLengths() throws Exception {
        Integer[] invalidWordLengths = {0, 1, 2, 8, 10};
        for (Integer i: invalidWordLengths) {
            mockMvc.perform(post("/game/new").param("wordLength", i.toString()))
                .andExpect(status().isBadRequest());
        }
    }

    @Test void startNewGameInvalidWordLengthParams() throws Exception {
        String[] invalidWordLengths = {"one", "two", "banana"};
        for (String i: invalidWordLengths) {
            mockMvc.perform(post("/game/new").param("wordLength", i))
                .andExpect(status().isBadRequest());
        }
    };

    // API-005 The API returns the current game status.

    @Test void getGame() throws Exception {
        Integer[] validWordLengths = {4, 5, 6, 7};
        for (Integer i: validWordLengths) {
            mockMvc.perform(
                post("/game/new")
                    .param("wordLength", i.toString())
            )
            .andExpect(status().isCreated());

            mockMvc.perform(get("/game"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.numGuesses").value(0))
                .andExpect(jsonPath("$.hiddenWord").isNotEmpty())
                .andExpect(jsonPath("$.hiddenWord").value(hasLength(i)))
                .andExpect(jsonPath("$.winState").value(false));
        }
    }

    // API-008 The API returns appropriate errors for invalid requests.

    @Test void getGameNoGameCreated() throws Exception {

        mockMvc.perform(get("/game"))
        .andExpect(status().isNotFound());
    };

    // API-003 A client can submit a guess for an active game.

    @Test void makeGuess() throws Exception {
        //     private String[] hiddenWords = {"game", "house", "hidden", "differs"};

        for (Map.Entry<Integer, List<String>> entry : TestData.VALID_GUESSES.entrySet()) {
            Integer wordLength = entry.getKey();
            List<String> guesses = entry.getValue();
            mockMvc.perform(
                post("/game/new")
                    .param("wordLength", wordLength.toString())
            )
            .andExpect(status().isCreated());

            mockMvc.perform(post("/game/guess")
                .contentType(MediaType.TEXT_PLAIN)
                .content(guesses.get(0)))
            .andExpect(status().isOk());
            

            mockMvc.perform(get("/game"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.numGuesses").value(1))
            .andExpect(jsonPath("$.guessedWords", hasItem(guesses.get(0))));
        }
    }

    // API-006 The API rejects guesses that do not meet the game's requirements.

    @Test void makeInvalidGuess() throws Exception {
        mockMvc.perform(
                post("/game/new")
                    .param("wordLength", String.valueOf(4))
            )
            .andExpect(status().isCreated());

        mockMvc.perform(
            post("/game/guess")
            .contentType(MediaType.TEXT_PLAIN)
            .content("horse")
        )
        .andExpect(status().isBadRequest());
    }

    @Test void makeGuessWithoutGame() throws Exception {
        mockMvc.perform(
            post("/game/guess")
            .contentType(MediaType.TEXT_PLAIN)
            .content("horse")
        )
        .andExpect(status().isNotFound());
    }

    // API-007 The API prevents guesses after a game has ended.

    @Test void makeGuessAfterWinning() throws Exception {
        for (Map.Entry<Integer, List<String>> entry : TestData.WINNING_GAMES.entrySet()) {
            Integer wordLength = entry.getKey();
            List<String> guesses = entry.getValue();
            mockMvc.perform(post("/game/new").param("wordLength", wordLength.toString()));

            for (String guess: guesses) {
                mockMvc.perform(post("/game/guess")
                    .contentType(MediaType.TEXT_PLAIN)
                    .content(guess)
                ).andExpect(status().isOk());
            }
            mockMvc.perform(post("/game/guess")
                .contentType(MediaType.TEXT_PLAIN)
                .content(guesses.get(0))
            ).andExpect(status().isBadRequest());

            mockMvc.perform(get("/game"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.winState").value(true));
        }
    }

    @Test void makeGuessAfterLosing() throws Exception {
        for (Map.Entry<Integer, List<String>> entry : TestData.LOSING_GAMES.entrySet()) {
            Integer wordLength = entry.getKey();
            List<String> guesses = entry.getValue();
            mockMvc.perform(post("/game/new").param("wordLength", wordLength.toString()))
                .andExpect(status().isCreated());

            for (String guess: guesses) {
                if (guess.equals(guesses.getLast())) {
                    mockMvc.perform(post("/game/guess")
                        .contentType(MediaType.TEXT_PLAIN)
                        .content(guess)
                    ).andExpect(status().isBadRequest());
                } else {
                    mockMvc.perform(post("/game/guess")
                        .contentType(MediaType.TEXT_PLAIN)
                        .content(guess)
                    ).andExpect(status().isOk());
                } 
                
                mockMvc.perform(get("/game"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.winState").value(false));
            }
        }
    }

    // API-004 The API returns the evaluation of a submitted guess.

    @Test void getGuessEvaluation() throws Exception {
        for (Map.Entry<Integer, List<String>> entry : TestData.WINNING_GAMES.entrySet()) {
            Integer wordLength = entry.getKey();
            List<String> guesses = entry.getValue();
            Map<Integer, String> expectedEvaluation = TestData.EXAMPLE_EVALUATIONS.get(wordLength);

            mockMvc.perform(post("/game/new").param("wordLength", wordLength.toString()))
                .andExpect(status().isCreated());

            ResultActions result = mockMvc.perform(post("/game/guess")
                .contentType(MediaType.TEXT_PLAIN)
                .content(guesses.get(0)))
            .andExpect(status().isOk());
            
            for (Map.Entry<Integer, String> evaluation : expectedEvaluation.entrySet()) {
                result.andExpect(
                    jsonPath("$." + evaluation.getKey())
                        .value(evaluation.getValue())
                );
            }
        }
    }
}
