package org.example.repository;

import org.example.entity.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import jakarta.transaction.Transactional;
import java.util.Optional;

public interface PlayerRepository extends JpaRepository<Player, Long> {
    Optional<Player> findByName(String name);

    @Modifying
    @Transactional
    @Query("UPDATE Player p SET p.score = :score WHERE p.name = :name")
    int updateScoreByName(String name, int score);
}