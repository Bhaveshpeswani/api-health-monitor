package com.bhavesh.healthmonitor.controller;

import com.bhavesh.healthmonitor.dto.IncidentResponse;
import com.bhavesh.healthmonitor.service.IncidentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RestController
@RequestMapping("/api/incidents")
public class StatsController {
    private final IncidentService incidentService;

    public StatsController(IncidentService incidentService) {
        this.incidentService = incidentService;
    }

    @GetMapping("/open")
    public ResponseEntity<List<IncidentResponse>> getOpenIncidents() {
        return ResponseEntity.ok(incidentService.getAllOpenIncidents());
    }
}
