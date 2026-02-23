package com.bhavesh.healthmonitor.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class IncidentResponse {
    private Long id;
    private Long monitorId;
    private LocalDateTime startedAt;
    private LocalDateTime resolvedAt;
    private Long durationSeconds;
    private String status;
}
