package com.bhavesh.healthmonitor.dto;

import lombok.Data;

import java.time.LocalDateTime;


@Data
public class CheckResponse {
    private Long id;
    private Long monitorId;
    private Integer statusCode;
    private Long responseTimeMs;
    private Boolean isUp;
    private LocalDateTime checkedAt;
}
