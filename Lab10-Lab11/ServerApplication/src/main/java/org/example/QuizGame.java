package org.example;

import org.example.entity.Player;
import org.example.entity.Question;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.HashSet;
import java.util.Set;


public class QuizGame {
    private final List<Question> questions;
    private final Map<String, Player> players;
    private int currentQuestionIndex;
    private final Set<String> answeredPlayers;
    private final long timeLimitMillis = 15000;
    private long questionStartTime;
    private boolean questionActive;

    public QuizGame(List<Question> questions) {
        this.questions = questions;
        this.players = new HashMap<>();
        this.currentQuestionIndex = 0;
        this.answeredPlayers = new HashSet<>();
        this.questionActive = false;
    }

    public synchronized String join(String playerName) {
        if (playerName == null || playerName.isBlank()) {
            return "Invalid player name.";
        }

        if (players.containsKey(playerName)) {
            return "Player already exists.";
        }

        players.put(playerName, new Player(playerName));
        return "Player " + playerName + " joined the game.";
    }
    public synchronized Map<String, Player> getPlayers() {
        return players;
    }

    public synchronized String getCurrentQuestion() {
        if (questions.isEmpty()) {
            return "No questions available.";
        }

        if (currentQuestionIndex >= questions.size()) {
            return "Game over. No more questions.";
        }

        if (!questionActive) {
            startQuestionTimer();
        }

        long remainingSeconds = getRemainingSeconds();

        return questions.get(currentQuestionIndex).getQuestion()
                + "\nTime left: " + remainingSeconds + " seconds";
    }

    private long getRemainingSeconds() {
        long elapsed = System.currentTimeMillis() - questionStartTime;
        long remaining = timeLimitMillis - elapsed;

        if (remaining <= 0) {
            return 0;
        }

        return remaining / 1000;
    }

    public synchronized String submitAnswer(String playerName, String answer) {
        if (!players.containsKey(playerName)) {
            return "Player not found. Use: join <name>";
        }

        if (currentQuestionIndex >= questions.size()) {
            return "Game over. No more questions.";
        }
        if (!questionActive) {
            startQuestionTimer();
        }


        if (answeredPlayers.contains(playerName)) {
            return "Player " + playerName + " already answered this question.";
        }
        long responseTime = System.currentTimeMillis() - questionStartTime;

        if (responseTime > timeLimitMillis) {
            answeredPlayers.add(playerName);
            return "Time expired for " + playerName + ". Answer not accepted.";
        }


        Question currentQuestion = questions.get(currentQuestionIndex);
        Player player = players.get(playerName);

        boolean correct = currentQuestion.verifyAnswer(answer);

        if (correct) {
            player.increaseScore();
        }

        player.addResponseTime(responseTime);

        answeredPlayers.add(playerName);

        StringBuilder result = new StringBuilder();

        if (correct) {
            result.append("Correct answer! ");
        } else {
            result.append("Wrong answer! ");
        }
        result.append(playerName)
                .append(" score: ")
                .append(player.getScore())
                .append(", response time: ")
                .append(responseTime / 1000.0)
                .append(" seconds")
                .append(", total time: ")
                .append(player.getTotalResponseTime() / 1000.0)
                .append(" seconds");
        if (answeredPlayers.size() == players.size()) {
            currentQuestionIndex++;
            answeredPlayers.clear();

            if (currentQuestionIndex < questions.size()) {
                startQuestionTimer();
                result.append("\nAll players answered. Moving to the next question.");
            } else {
                questionActive = false;
                result.append("\nAll players answered. Game over.");
                result.append("\n").append(getWinner());
            }
        } else {
            result.append("\nWaiting for the other players to answer.");
        }

        return result.toString();
    }
    public synchronized String getWinner() {
        if (players.isEmpty()) {
            return "No players joined yet.";
        }

        Player winner = null;

        for (Player player : players.values()) {
            if (winner == null) {
                winner = player;
            } else if (player.getScore() > winner.getScore()) {
                winner = player;
            } else if (player.getScore() == winner.getScore()
                    && player.getTotalResponseTime() < winner.getTotalResponseTime()) {
                winner = player;
            }
        }

        if (winner == null || winner.getScore() == 0) {
            return "No winner!";
        }

        return "Winner: " + winner.getName()
                + " with score " + winner.getScore()
                + " and total response time "
                + winner.getTotalResponseTime() / 1000.0
                + " seconds.";
    }

    public synchronized String checkTimeExpired() {
        if (!questionActive) {
            return null;
        }

        if (questions.isEmpty() || currentQuestionIndex >= questions.size()) {
            return null;
        }

        long elapsed = System.currentTimeMillis() - questionStartTime;

        if (elapsed < timeLimitMillis) {
            return null;
        }

        currentQuestionIndex++;
        answeredPlayers.clear();

        if (currentQuestionIndex >= questions.size()) {
            questionActive = false;
            return "Time expired. Game over.";
        }

        startQuestionTimer();

        return "Time expired. Moving to the next question:\n"
                + questions.get(currentQuestionIndex).getQuestion()
                + "\nTime left: " + getRemainingSeconds() + " seconds";
    }

    public synchronized String getScores() {
        if (players.isEmpty()) {
            return "No players joined yet.";
        }

        StringBuilder result = new StringBuilder("Scores:\n");

        for (Player player : players.values()) {
            result.append(player.getName())
                    .append(": ")
                    .append(player.getScore())
                    .append("\n");
        }

        return result.toString();
    }

    public synchronized String getHelp() {
        return """
                Available commands:
                join <name>
                question
                answer <A/B/C/D>
                scores
                stop
                winner
                 Each question has 15 seconds.
                """;
    }


    private void startQuestionTimer() {
        questionStartTime = System.currentTimeMillis();
        questionActive = true;
    }

}