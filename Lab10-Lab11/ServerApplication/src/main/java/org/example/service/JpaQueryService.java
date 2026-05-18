package org.example.service;

import org.example.entity.Result;
import org.example.repository.PlayerRepository;
import org.example.repository.ResultRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class JpaQueryService {
    private static final Logger logger = LoggerFactory.getLogger(JpaQueryService.class);

    private final ResultRepository resultRepository;
    private final PlayerRepository playerRepository;

    public JpaQueryService(ResultRepository resultRepository, PlayerRepository playerRepository) {
        this.resultRepository = resultRepository;
        this.playerRepository = playerRepository;
    }

    public List<Result> getWinningResultsWithLogging() {
        long start = System.nanoTime();

        try {
            logger.info("JPQL SELECT findWinningResults started");

            List<Result> results = resultRepository.findWinningResults();

            long duration = System.nanoTime() - start;

            logger.info("JPQL SELECT findWinningResults executed in {} ms",
                    TimeUnit.NANOSECONDS.toMillis(duration));

            return results;
        } catch (Exception e) {
            long duration = System.nanoTime() - start;

            logger.error("Exception during JPQL SELECT after {} ms: {}",
                    TimeUnit.NANOSECONDS.toMillis(duration),
                    e.getMessage(),
                    e);

            throw e;
        }
    }

    public void updatePlayerScoreWithLogging(String name, int score) {
        long start = System.nanoTime();

        try {
            logger.info("JPQL UPDATE updateScoreByName started");

            int updatedRows = playerRepository.updateScoreByName(name, score);

            long duration = System.nanoTime() - start;

            logger.info("JPQL UPDATE updateScoreByName({}, {}) affected {} rows and executed in {} ms",
                    name,
                    score,
                    updatedRows,
                    TimeUnit.NANOSECONDS.toMillis(duration));
        } catch (Exception e) {
            long duration = System.nanoTime() - start;

            logger.error("Exception during JPQL UPDATE after {} ms: {}",
                    TimeUnit.NANOSECONDS.toMillis(duration),
                    e.getMessage(),
                    e);

            throw e;
        }
    }
}