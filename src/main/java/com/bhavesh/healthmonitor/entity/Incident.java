package com.bhavesh.healthmonitor.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "incidents")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Incident {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long monitorId;

    @Column(nullable = false)
    private LocalDateTime startedAt;

    private LocalDateTime resolvedAt;

    private Long durationSeconds;

    @Column(nullable = false)
    private String status = "OPEN"; // OPEN, [RESOLVED]

    @PrePersist
    public void prePersist() {
        this.startedAt = LocalDateTime.now();
        if (this.status == null) this.status = "OPEN";
    }
}
