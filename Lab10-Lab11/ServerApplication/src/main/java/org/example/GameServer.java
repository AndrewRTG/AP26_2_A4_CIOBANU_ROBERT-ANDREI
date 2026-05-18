package org.example;

import org.example.entity.Game;
import org.example.entity.Player;
import org.example.entity.Question;
import org.example.entity.Result;
import org.example.repository.GameRepository;
import org.example.repository.PlayerRepository;
import org.example.repository.QuestionRepository;
import org.example.repository.ResultRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Component
public class GameServer implements CommandLineRunner {
    public static final int PORT = 2026;

    private ServerSocket serverSocket;
    private volatile boolean running = true;

    private final PlayerRepository playerRepository;
    private final QuestionRepository questionRepository;
    private final GameRepository gameRepository;
    private final ResultRepository resultRepository;

    private Game currentGame;
    private QuizGame quizGame;
    private ExecutorService threadPool;
    private Set<ClientThread> clients;
    private ScheduledExecutorService timerExecutor;
    private boolean resultsSaved = false;

    public GameServer(PlayerRepository playerRepository,
                      QuestionRepository questionRepository,
                      GameRepository gameRepository,
                      ResultRepository resultRepository) {
        this.playerRepository = playerRepository;
        this.questionRepository = questionRepository;
        this.gameRepository = gameRepository;
        this.resultRepository = resultRepository;
    }

    @Override
    public void run(String... args) {
        startServer();
    }

    private void startServer() {
        initializeQuestionsIfNeeded();

        List<Question> questions = questionRepository.findAll();

        this.currentGame = gameRepository.save(new Game("RUNNING"));
        this.quizGame = new QuizGame(questions);

        this.threadPool = Executors.newFixedThreadPool(8);
        this.timerExecutor = Executors.newSingleThreadScheduledExecutor();
        this.clients = ConcurrentHashMap.newKeySet();

        Runtime.getRuntime().addShutdownHook(new Thread(this::stopServer));

        try {
            serverSocket = new ServerSocket(PORT);

            timerExecutor.scheduleAtFixedRate(() -> {
                String message = quizGame.checkTimeExpired();

                if (message != null) {
                    broadcast(message);

                    if (message.contains("Game over.")) {
                        saveFinalResults();
                    }
                }
            }, 1, 1, TimeUnit.SECONDS);

            System.out.println("GameServer started. Listening on port " + PORT + "...");

            while (running) {
                try {
                    System.out.println("Waiting for a client ...");

                    Socket socket = serverSocket.accept();
                    System.out.println("Client connected: " + socket.getInetAddress());

                    ClientThread clientThread = new ClientThread(socket, this, quizGame);
                    clients.add(clientThread);

                    threadPool.execute(clientThread);

                } catch (SocketException e) {
                    if (running) {
                        System.err.println("Socket error: " + e);
                    } else {
                        System.out.println("Server socket closed.");
                    }
                }
            }

        } catch (IOException e) {
            System.err.println("Server error: " + e.getMessage());

        } finally {
            saveFinalResults();

            if (timerExecutor != null) {
                timerExecutor.shutdownNow();
            }

            if (threadPool != null) {
                shutdownThreadPool();
            }

            closeServerSocket();
            System.out.println("Server has successfully shut down.");
        }
    }

    private void initializeQuestionsIfNeeded() {
        if (questionRepository.count() > 0) {
            return;
        }

        List<Question> questions = QuestionLoader.loadQuestions("questions.txt");
        questionRepository.saveAll(questions);

        System.out.println("Questions inserted into database: " + questions.size());
    }

    public void savePlayerIfNeeded(String playerName) {
        playerRepository.findByName(playerName)
                .orElseGet(() -> playerRepository.save(new Player(playerName)));
    }

    public synchronized void saveFinalResults() {
        if (resultsSaved || currentGame == null || quizGame == null) {
            return;
        }

        Map<String, Player> players = quizGame.getPlayers();

        if (players.isEmpty()) {
            return;
        }

        String winnerMessage = quizGame.getWinner();

        for (Player playerFromGame : players.values()) {
            Player dbPlayer = playerRepository.findByName(playerFromGame.getName())
                    .orElseGet(() -> playerRepository.save(new Player(playerFromGame.getName())));

            dbPlayer.setScore(playerFromGame.getScore());
            dbPlayer.setTotalResponseTime(playerFromGame.getTotalResponseTime());
            playerRepository.save(dbPlayer);

            boolean isWinner = winnerMessage.contains("Winner: " + playerFromGame.getName());

            Result result = new Result(
                    currentGame,
                    dbPlayer,
                    playerFromGame.getScore(),
                    playerFromGame.getTotalResponseTime(),
                    isWinner
            );

            resultRepository.save(result);
        }

        currentGame.finish();
        gameRepository.save(currentGame);

        resultsSaved = true;
        System.out.println("Final results saved in database.");
    }

    public void broadcast(String message) {
        if (clients == null) {
            return;
        }

        for (ClientThread client : clients) {
            client.sendMessage(message);
        }
    }

    public void removeClient(ClientThread clientThread) {
        if (clients != null) {
            clients.remove(clientThread);
        }
    }

    public void stopServer() {
        if (!running) {
            return;
        }

        running = false;

        broadcast("Server stopped.");

        if (clients != null) {
            for (ClientThread client : clients) {
                client.closeConnection();
            }
        }

        closeServerSocket();
    }

    private void closeServerSocket() {
        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
        } catch (IOException e) {
            System.err.println("Error closing ServerSocket: " + e.getMessage());
        }
    }

    private void shutdownThreadPool() {
        threadPool.shutdown();

        try {
            if (!threadPool.awaitTermination(5, TimeUnit.SECONDS)) {
                System.out.println("Some client tasks are still running. Forcing shutdown...");
                threadPool.shutdownNow();
            }
        } catch (InterruptedException e) {
            threadPool.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}