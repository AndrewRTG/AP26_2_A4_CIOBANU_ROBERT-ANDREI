package org.example.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "players")
public class Player extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private int score;

    private long totalResponseTime;

    @OneToMany(mappedBy = "player", cascade = CascadeType.ALL)
    private List<Result> results = new ArrayList<>();

    public Player(String name) {
        this.name = name;
        this.totalResponseTime = 0;
    }

    public void increaseScore() {
        score++;
    }

    public void addResponseTime(long responseTime) {
        totalResponseTime += responseTime;
    }

    public void resetScore() {
        score = 0;
        totalResponseTime = 0;
    }
}