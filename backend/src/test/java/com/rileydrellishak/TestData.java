package com.rileydrellishak;

import java.util.List;
import java.util.Map;

public class TestData {
    public static final Map<Integer, List<String>> VALID_GUESSES = Map.of(
        4, List.of("word", "test", "game"),
        5, List.of("hello", "world", "apple"),
        6, List.of("banana", "yellow", "little"),
        7, List.of("another", "snowman", "testing")
    );

    public static final Map<Integer, List<String>> WINNING_GAMES = Map.of(
        4, List.of("test", "word", "game"),
        5, List.of("hello", "world", "house"),
        6, List.of("banana", "little", "hidden"),
        7, List.of("another", "testing", "differs")
    );

    public static final Map<Integer, List<String>> LOSING_GAMES = Map.of(
        4, List.of("test", "word", "look", "fish", "tree", "milk"),
        5, List.of("hello", "world", "apple", "train", "plant", "music"),
        6, List.of("banana", "little", "yellow", "people", "friend", "school"),
        7, List.of("another", "testing", "nothing", "working", "picture", "journey")
    );

    public static final Map<Integer, Map<Integer, String>> EXAMPLE_EVALUATIONS = Map.of(
        // test vs game
        4, Map.of(0, "absent", 1, "present", 2, "absent", 3, "absent"),
        //hello vs house
        5, Map.of(0, "correct", 1, "present", 2, "absent", 3, "absent", 4, "present"),
        //banana vs hidden
        6, Map.of(0, "absent", 1, "absent", 2, "present", 3, "absent", 4, "absent", 5, "absent"),
        // another vs differs
        7, Map.of(0, "absent", 1, "absent", 2, "absent", 3, "absent", 4, "absent", 5, "present", 6, "present")
    );
}
