package com.quiz.app;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javafx.animation.Animation;
import javafx.animation.AnimationTimer;
import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.PauseTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;

public class QuizApp extends Application {
    private static final int QUESTIONS_PER_ROUND = 10;

    private final QuizEngine engine = new QuizEngine(QUESTIONS_PER_ROUND);
    private final Random random = new Random();

    private StackPane root;
    private StackPane centerContainer;
    private VBox questionCard;
    private Pane cursorLayer;
    private Circle cursorShadow;

    private Label questionCounterLabel;
    private Label scoreLabel;
    private Label subjectLabel;
    private Label promptLabel;
    private Label feedbackLabel;
    private VBox optionsBox;

    private boolean answerLocked;
    private AnimationTimer cursorFollowerTimer;
    private double cursorX;
    private double cursorY;
    private double targetCursorX;
    private double targetCursorY;

    @Override
    public void start(Stage stage) {
        root = new StackPane();

        BorderPane layout = new BorderPane();
        layout.setPadding(new Insets(26));
        layout.setMaxWidth(980);

        layout.setTop(createHeader());

        centerContainer = new StackPane();
        centerContainer.setPadding(new Insets(24, 10, 10, 10));
        questionCard = createQuestionCard();
        centerContainer.getChildren().add(questionCard);
        layout.setCenter(centerContainer);

        root.getChildren().add(layout);

        Scene scene = new Scene(root, 980, 700);
        scene.getStylesheets().add(getClass().getResource("/styles/app.css").toExternalForm());
        setupCursorFollower(scene);

        stage.setTitle("College Quest Quiz");
        try (InputStream iconStream = getClass().getResourceAsStream("/icons/quiz-app.png")) {
            if (iconStream != null) {
                stage.getIcons().add(new Image(iconStream));
            }
        } catch (IOException ignored) {
            // Icon loading is optional for runtime behavior.
        }
        stage.setMinWidth(860);
        stage.setMinHeight(620);
        stage.setScene(scene);
        stage.show();

        renderCurrentQuestion();
        playEntranceAnimation(questionCard);
    }

    private Node createHeader() {
        Label title = new Label("College Quest");
        title.getStyleClass().add("app-title");

        Label subtitle = new Label("Randomized multi-subject challenge");
        subtitle.getStyleClass().add("app-subtitle");

        VBox titleBlock = new VBox(2, title, subtitle);
        titleBlock.setAlignment(Pos.CENTER_LEFT);

        questionCounterLabel = new Label();
        questionCounterLabel.getStyleClass().add("score-pill");

        scoreLabel = new Label();
        scoreLabel.getStyleClass().add("score-pill");

        HBox scoreBlock = new HBox(10, questionCounterLabel, scoreLabel);
        scoreBlock.setAlignment(Pos.CENTER_RIGHT);

        BorderPane header = new BorderPane();
        header.getStyleClass().add("header-bar");
        header.setLeft(titleBlock);
        header.setRight(scoreBlock);

        return header;
    }

    private VBox createQuestionCard() {
        subjectLabel = new Label();
        subjectLabel.getStyleClass().add("subject-chip");

        promptLabel = new Label();
        promptLabel.getStyleClass().add("question-text");
        promptLabel.setWrapText(true);
        promptLabel.setMinHeight(110);

        optionsBox = new VBox(12);

        feedbackLabel = new Label("Choose your answer to continue");
        feedbackLabel.getStyleClass().add("feedback-text");

        VBox card = new VBox(18, subjectLabel, promptLabel, optionsBox, feedbackLabel);
        card.getStyleClass().add("question-card");

        return card;
    }

    private void renderCurrentQuestion() {
        answerLocked = false;
        clearFeedbackTone();
        feedbackLabel.setText("Choose your answer to continue");

        questionCounterLabel.setText("Question " + engine.getCurrentQuestionNumber() + " / " + engine.getTotalQuestions());
        scoreLabel.setText("Score " + engine.getScore());

        QuizQuestion question = engine.getCurrentQuestion();
        subjectLabel.setText(question.subject());
        promptLabel.setText(question.prompt());

        optionsBox.getChildren().clear();

        List<String> options = engine.shuffledOptionsForCurrentQuestion();
        for (String option : options) {
            Button button = new Button(option);
            button.getStyleClass().add("option-button");
            button.setWrapText(true);
            button.setMaxWidth(Double.MAX_VALUE);
            button.setOnAction(event -> handleAnswer(question, option, button));

            optionsBox.getChildren().add(button);
            VBox.setVgrow(button, Priority.NEVER);
        }
    }

    private void handleAnswer(QuizQuestion question, String selectedOption, Button selectedButton) {
        if (answerLocked) {
            return;
        }
        answerLocked = true;

        setButtonsDisabled(true);

        QuizEngine.AnswerResult result = engine.submitAnswer(selectedOption);
        scoreLabel.setText("Score " + engine.getScore());

        if (result.correct()) {
            selectedButton.getStyleClass().add("option-correct");
            setFeedback("Excellent! That answer is correct.", true);
            playCorrectCelebration(() -> continueAfterAnswer(result));
            return;
        }

        selectedButton.getStyleClass().add("option-wrong");
        Button correctButton = findOptionButtonByText(question.correctAnswer());
        if (correctButton != null && correctButton != selectedButton) {
            correctButton.getStyleClass().add("option-correct");
        }

        setFeedback("Not quite. Correct answer: " + question.correctAnswer(), false);
        playWrongShake(selectedButton);

        PauseTransition wait = new PauseTransition(Duration.seconds(1.45));
        wait.setOnFinished(event -> continueAfterAnswer(result));
        wait.play();
    }

    private void continueAfterAnswer(QuizEngine.AnswerResult result) {
        if (result.finished()) {
            showResultScreen();
            return;
        }

        FadeTransition fadeOut = new FadeTransition(Duration.millis(230), questionCard);
        fadeOut.setToValue(0.12);
        fadeOut.setOnFinished(event -> {
            renderCurrentQuestion();
            questionCard.setOpacity(0.12);

            FadeTransition fadeIn = new FadeTransition(Duration.millis(280), questionCard);
            fadeIn.setToValue(1.0);
            fadeIn.play();
        });
        fadeOut.play();
    }

    private void showResultScreen() {
        questionCounterLabel.setText("Completed");
        scoreLabel.setText("Final score " + engine.getScore() + " / " + engine.getTotalQuestions());

        VBox resultCard = createResultCard();

        FadeTransition fadeOut = new FadeTransition(Duration.millis(220), questionCard);
        fadeOut.setToValue(0.0);
        fadeOut.setOnFinished(event -> {
            centerContainer.getChildren().setAll(resultCard);
            resultCard.setOpacity(0);
            resultCard.setScaleX(0.92);
            resultCard.setScaleY(0.92);

            FadeTransition fadeIn = new FadeTransition(Duration.millis(320), resultCard);
            fadeIn.setFromValue(0);
            fadeIn.setToValue(1);

            ScaleTransition scaleIn = new ScaleTransition(Duration.millis(320), resultCard);
            scaleIn.setToX(1.0);
            scaleIn.setToY(1.0);

            new ParallelTransition(fadeIn, scaleIn).play();
        });
        fadeOut.play();
    }

    private VBox createResultCard() {
        int score = engine.getScore();
        int total = engine.getTotalQuestions();
        double percent = (score * 100.0) / total;

        String heading;
        String summary;
        if (percent >= 90) {
            heading = "Brilliant Performance";
            summary = "Outstanding depth across subjects.";
        } else if (percent >= 70) {
            heading = "Strong Work";
            summary = "Great command with room for one more leap.";
        } else if (percent >= 50) {
            heading = "Solid Foundation";
            summary = "You are building momentum. Keep going.";
        } else {
            heading = "Keep Practicing";
            summary = "Every attempt sharpens your knowledge.";
        }

        Label headingLabel = new Label(heading);
        headingLabel.getStyleClass().add("result-heading");

        Label resultScoreLabel = new Label(score + " / " + total);
        resultScoreLabel.getStyleClass().add("result-score");

        Label percentLabel = new Label(String.format("Accuracy %.0f%%", percent));
        percentLabel.getStyleClass().add("result-percent");

        Label summaryLabel = new Label(summary);
        summaryLabel.getStyleClass().add("result-summary");
        summaryLabel.setWrapText(true);

        Button restartButton = new Button("Play Another 10 Questions");
        restartButton.getStyleClass().add("restart-button");
        restartButton.setOnAction(event -> restartRound());

        VBox resultCard = new VBox(14, headingLabel, resultScoreLabel, percentLabel, summaryLabel, restartButton);
        resultCard.getStyleClass().add("result-card");
        resultCard.setAlignment(Pos.CENTER);

        return resultCard;
    }

    private void restartRound() {
        engine.startNewSession();
        centerContainer.getChildren().setAll(questionCard);
        questionCard.setOpacity(1);
        questionCard.setScaleX(1);
        questionCard.setScaleY(1);
        renderCurrentQuestion();
        playEntranceAnimation(questionCard);
    }

    private void playCorrectCelebration(Runnable onFinished) {
        StackPane overlay = new StackPane();
        overlay.getStyleClass().add("celebration-overlay");
        overlay.setMouseTransparent(true);

        Label congratulationLabel = new Label("Correct! Beautiful work!");
        congratulationLabel.getStyleClass().add("celebration-label");
        congratulationLabel.setOpacity(0);
        overlay.getChildren().add(congratulationLabel);

        List<Animation> particleAnimations = new ArrayList<>();
        Color[] palette = {
                Color.web("#F4A261"),
                Color.web("#2A9D8F"),
                Color.web("#E9C46A"),
                Color.web("#8AB17D"),
                Color.web("#6CA6C1")
        };

        for (int i = 0; i < 22; i++) {
            Circle particle = new Circle(3.0 + random.nextDouble() * 3.5);
            particle.setFill(palette[random.nextInt(palette.length)]);
            particle.setOpacity(0.95);
            overlay.getChildren().add(particle);

            double angle = Math.toRadians((360.0 / 22) * i + random.nextDouble() * 15);
            double distance = 120 + random.nextDouble() * 170;

            TranslateTransition explode = new TranslateTransition(Duration.millis(580 + random.nextInt(220)), particle);
            explode.setByX(Math.cos(angle) * distance);
            explode.setByY(Math.sin(angle) * distance - 70);

            FadeTransition fade = new FadeTransition(Duration.millis(680 + random.nextInt(220)), particle);
            fade.setFromValue(0.95);
            fade.setToValue(0.0);

            ScaleTransition shrink = new ScaleTransition(Duration.millis(680), particle);
            shrink.setToX(0.25);
            shrink.setToY(0.25);

            particleAnimations.add(new ParallelTransition(explode, fade, shrink));
        }

        FadeTransition labelFadeIn = new FadeTransition(Duration.millis(200), congratulationLabel);
        labelFadeIn.setFromValue(0);
        labelFadeIn.setToValue(1);

        ScaleTransition labelPop = new ScaleTransition(Duration.millis(250), congratulationLabel);
        labelPop.setFromX(0.72);
        labelPop.setFromY(0.72);
        labelPop.setToX(1.0);
        labelPop.setToY(1.0);

        ParallelTransition burst = new ParallelTransition();
        burst.getChildren().addAll(particleAnimations);
        burst.getChildren().addAll(labelFadeIn, labelPop);

        PauseTransition hold = new PauseTransition(Duration.millis(450));

        FadeTransition overlayFadeOut = new FadeTransition(Duration.millis(260), overlay);
        overlayFadeOut.setToValue(0);

        SequentialTransition sequence = new SequentialTransition(burst, hold, overlayFadeOut);
        sequence.setOnFinished(event -> {
            root.getChildren().remove(overlay);
            onFinished.run();
        });

        root.getChildren().add(overlay);
        sequence.play();
    }

    private void playWrongShake(Button node) {
        TranslateTransition shake = new TranslateTransition(Duration.millis(65), node);
        shake.setByX(9);
        shake.setCycleCount(6);
        shake.setAutoReverse(true);
        shake.play();
    }

    private void setupCursorFollower(Scene scene) {
        cursorLayer = new Pane();
        cursorLayer.getStyleClass().add("cursor-layer");
        cursorLayer.setManaged(false);
        cursorLayer.setMouseTransparent(true);
        cursorLayer.setPickOnBounds(false);
        cursorLayer.prefWidthProperty().bind(root.widthProperty());
        cursorLayer.prefHeightProperty().bind(root.heightProperty());

        cursorShadow = new Circle(95);
        cursorShadow.getStyleClass().add("cursor-shadow");
        cursorShadow.setManaged(false);
        cursorShadow.setMouseTransparent(true);
        cursorShadow.setOpacity(0.0);
        cursorLayer.getChildren().add(cursorShadow);

        root.getChildren().add(0, cursorLayer);

        cursorX = scene.getWidth() * 0.5;
        cursorY = scene.getHeight() * 0.5;
        targetCursorX = cursorX;
        targetCursorY = cursorY;
        cursorShadow.setCenterX(cursorX);
        cursorShadow.setCenterY(cursorY);

        scene.addEventFilter(MouseEvent.MOUSE_MOVED, event -> {
            targetCursorX = event.getSceneX();
            targetCursorY = event.getSceneY();
        });

        scene.addEventFilter(MouseEvent.MOUSE_DRAGGED, event -> {
            targetCursorX = event.getSceneX();
            targetCursorY = event.getSceneY();
        });

        scene.addEventFilter(MouseEvent.MOUSE_ENTERED, event -> animateCursorShadowOpacity(0.42));
        scene.addEventFilter(MouseEvent.MOUSE_EXITED, event -> animateCursorShadowOpacity(0.0));

        cursorFollowerTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                // Smooth lag creates a soft, reactive trailing shadow instead of a hard cursor lock.
                cursorX += (targetCursorX - cursorX) * 0.12;
                cursorY += (targetCursorY - cursorY) * 0.12;
                cursorShadow.setCenterX(cursorX);
                cursorShadow.setCenterY(cursorY);
            }
        };
        cursorFollowerTimer.start();
    }

    private void animateCursorShadowOpacity(double targetOpacity) {
        FadeTransition fade = new FadeTransition(Duration.millis(280), cursorShadow);
        fade.setToValue(targetOpacity);
        fade.play();
    }

    private void playEntranceAnimation(Node node) {
        node.setOpacity(0.0);
        node.setTranslateY(14);

        FadeTransition fade = new FadeTransition(Duration.millis(360), node);
        fade.setToValue(1.0);

        TranslateTransition slide = new TranslateTransition(Duration.millis(360), node);
        slide.setToY(0);

        new ParallelTransition(fade, slide).play();
    }

    private void setButtonsDisabled(boolean disabled) {
        for (Node child : optionsBox.getChildren()) {
            if (child instanceof Button button) {
                button.setDisable(disabled);
            }
        }
    }

    private Button findOptionButtonByText(String text) {
        for (Node child : optionsBox.getChildren()) {
            if (child instanceof Button button && button.getText().equals(text)) {
                return button;
            }
        }
        return null;
    }

    private void setFeedback(String message, boolean isCorrect) {
        clearFeedbackTone();
        feedbackLabel.getStyleClass().add(isCorrect ? "feedback-correct" : "feedback-wrong");
        feedbackLabel.setText(message);
    }

    private void clearFeedbackTone() {
        feedbackLabel.getStyleClass().removeAll("feedback-correct", "feedback-wrong");
    }

    @Override
    public void stop() {
        if (cursorFollowerTimer != null) {
            cursorFollowerTimer.stop();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
