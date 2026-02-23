package com.bhavesh.healthmonitor.dto;

import lombok.Data;

@Data
public class MonitorRequest {
    private String name;
    private String url;
    private Integer checkIntervalSeconds;
}
