package com.bhavesh.healthmonitor.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "monitors")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Monitor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String url;

    @Column(nullable = false)
    private Integer checkIntervalSeconds = 60;

    @Column(nullable = false)
    private String status = "ACTIVE"; // ACTIVE, PAUSED

    @Column(nullable = false)
    private String healthStatus = "HEALTHY"; // HEALTHY, DEGRADED, DOWN

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        if (this.status == null) this.status = "ACTIVE";
        if (this.healthStatus == null) this.healthStatus = "HEALTHY";
        if (this.checkIntervalSeconds == null) this.checkIntervalSeconds = 60;
    }
}
