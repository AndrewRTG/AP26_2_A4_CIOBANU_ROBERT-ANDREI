package org.example.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
@Getter
@Setter
@NoArgsConstructor
@MappedSuperclass
@EntityListeners(AuditListener.class)
public abstract class Auditable {
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private String lastOperation;


}