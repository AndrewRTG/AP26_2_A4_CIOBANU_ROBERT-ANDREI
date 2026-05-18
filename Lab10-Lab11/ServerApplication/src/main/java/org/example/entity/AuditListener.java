package org.example.entity;

import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

import java.time.LocalDateTime;

public class AuditListener {
    @PrePersist
    public void beforeInsert(Object object) {
        if (object instanceof Auditable auditable) {
            auditable.setCreatedAt(LocalDateTime.now());
            auditable.setUpdatedAt(LocalDateTime.now());
            auditable.setLastOperation("INSERT");
        }
    }

    @PreUpdate
    public void beforeUpdate(Object object) {
        if (object instanceof Auditable auditable) {
            auditable.setUpdatedAt(LocalDateTime.now());
            auditable.setLastOperation("UPDATE");
        }
    }
}