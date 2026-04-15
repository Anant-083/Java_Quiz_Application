package com.quiz.app;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public final class QuizEngine {
    private final Random random = new Random();
    private final int totalQuestions;
    private List<QuizQuestion> sessionQuestions = List.of();
    private int currentIndex;
    private int score;

    public QuizEngine(int totalQuestions) {
        if (totalQuestions <= 0) {
            throw new IllegalArgumentException("totalQuestions must be greater than 0");
        }
        this.totalQuestions = totalQuestions;
        startNewSession();
    }

    public void startNewSession() {
        List<QuizQuestion> bank = new ArrayList<>(QuestionBank.allQuestions());
        if (bank.size() < totalQuestions) {
            throw new IllegalStateException("Question bank has fewer questions than required total");
        }

        Collections.shuffle(bank, random);
        sessionQuestions = List.copyOf(bank.subList(0, totalQuestions));
        currentIndex = 0;
        score = 0;
    }

    public int getScore() {
        return score;
    }

    public int getTotalQuestions() {
        return totalQuestions;
    }

    public int getCurrentQuestionNumber() {
        ensureQuestionAvailable();
        return currentIndex + 1;
    }

    public boolean isFinished() {
        return currentIndex >= totalQuestions;
    }

    public QuizQuestion getCurrentQuestion() {
        ensureQuestionAvailable();
        return sessionQuestions.get(currentIndex);
    }

    public List<String> shuffledOptionsForCurrentQuestion() {
        List<String> shuffled = new ArrayList<>(getCurrentQuestion().options());
        Collections.shuffle(shuffled, random);
        return shuffled;
    }

    public AnswerResult submitAnswer(String selectedOption) {
        Objects.requireNonNull(selectedOption, "selectedOption cannot be null");
        QuizQuestion question = getCurrentQuestion();

        boolean correct = question.correctAnswer().equals(selectedOption);
        if (correct) {
            score++;
        }
        currentIndex++;

        return new AnswerResult(correct, question.correctAnswer(), score, currentIndex, isFinished());
    }

    private void ensureQuestionAvailable() {
        if (isFinished()) {
            throw new IllegalStateException("No active question. Session has ended.");
        }
    }

    public record AnswerResult(
            boolean correct,
            String correctAnswer,
            int score,
            int answeredCount,
            boolean finished
    ) {
    }
}
