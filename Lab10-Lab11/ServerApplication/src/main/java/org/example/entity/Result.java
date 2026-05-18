package org.example.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "results")
public class Result extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "game_id")
    private Game game;

    @ManyToOne
    @JoinColumn(name = "player_id")
    private Player player;

    private int score;

    private long totalResponseTime;

    private boolean winner;

    public Result(Game game, Player player, int score, long totalResponseTime, boolean winner) {
        this.game = game;
        this.player = player;
        this.score = score;
        this.totalResponseTime = totalResponseTime;
        this.winner = winner;
    }


}