package com.bhavesh.healthmonitor.dto;

import lombok.Data;

@Data
public class StatsResponse {
    private Long monitorId;
    private String monitorName;
    private String healthStatus;
    private Long totalChecks;
    private Long upChecks;
    private Long downChecks;
    private Double uptimePercentage;
    private Double averageResponseTimeMs;
    private Long totalIncidents;
    private Long openIncidents;
    private String lastCheckedAt;
}
