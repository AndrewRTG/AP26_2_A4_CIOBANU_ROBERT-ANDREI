package org.example;

import org.example.entity.Player;
import org.example.entity.Result;
import org.example.repository.PlayerRepository;
import org.example.service.JpaQueryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(properties = {
        "spring.datasource.url=jdbc:h2:mem:testdb",
        "spring.jpa.hibernate.ddl-auto=create-drop"
})
public class JpaHomeworkTest {

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private JpaQueryService jpaQueryService;

    @Test
    void testModifyingQuery() {
        assertNotNull(jpaQueryService);

        Player player = new Player("test_player");
        playerRepository.save(player);

        jpaQueryService.updatePlayerScoreWithLogging("test_player", 10);

        Player updatedPlayer = playerRepository.findByName("test_player").orElseThrow();

        assertEquals(10, updatedPlayer.getScore());
    }

    @Test
    void testReadQueryWithLogging() {
        assertNotNull(jpaQueryService);

        List<Result> results = jpaQueryService.getWinningResultsWithLogging();

        assertNotNull(results);
    }
}