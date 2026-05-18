package org.example.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "games")
public class Game extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime startedAt;

    private LocalDateTime endedAt;

    private String status;

    @OneToMany(mappedBy = "game", cascade = CascadeType.ALL)
    private List<Result> results = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "games_questions",
            joinColumns = @JoinColumn(name = "game_id"),
            inverseJoinColumns = @JoinColumn(name = "question_id")
    )
    private List<Question> questions = new ArrayList<>();

    public Game(String status) {
        this.startedAt = LocalDateTime.now();
        this.status = status;
    }

    public void finish() {
        this.endedAt = LocalDateTime.now();
        this.status = "FINISHED";
    }
}