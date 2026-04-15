package com.quiz.app;

import java.util.List;
import java.util.Objects;

public record QuizQuestion(String subject, String prompt, List<String> options, String correctAnswer) {
    public QuizQuestion {
        Objects.requireNonNull(subject, "subject cannot be null");
        Objects.requireNonNull(prompt, "prompt cannot be null");
        Objects.requireNonNull(options, "options cannot be null");
        Objects.requireNonNull(correctAnswer, "correctAnswer cannot be null");

        options = List.copyOf(options);
        if (options.size() < 2) {
            throw new IllegalArgumentException("At least two options are required");
        }
        if (!options.contains(correctAnswer)) {
            throw new IllegalArgumentException("Correct answer must exist in options");
        }
    }
}
