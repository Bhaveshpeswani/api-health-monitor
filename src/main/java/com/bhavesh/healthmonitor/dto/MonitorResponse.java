package com.bhavesh.healthmonitor.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MonitorResponse{
        private Long id;
        private String name;
        private String url;
        private Integer checkIntervalSeconds;
        private String status;
        private String healthStatus;
        private LocalDateTime createdAt;
}
