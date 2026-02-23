package com.bhavesh.healthmonitor.controller;

import com.bhavesh.healthmonitor.dto.*;
import com.bhavesh.healthmonitor.service.CheckService;
import com.bhavesh.healthmonitor.service.IncidentService;
import com.bhavesh.healthmonitor.service.MonitorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/monitors")
public class MonitorController {

    private final MonitorService monitorService;
    private final CheckService checkService;
    private final IncidentService incidentService;

    public MonitorController(MonitorService monitorService,
                             CheckService checkService,
                             IncidentService incidentService) {
        this.monitorService = monitorService;
        this.checkService = checkService;
        this.incidentService = incidentService;
    }

    @PostMapping
    public ResponseEntity<MonitorResponse> create(@RequestBody MonitorRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(monitorService.createMonitor(request));
    }

    @GetMapping
    public ResponseEntity<List<MonitorResponse>> getAll() {
        return ResponseEntity.ok(monitorService.getAllMonitors());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MonitorResponse> getOne(@PathVariable Long id) {
        return ResponseEntity.ok(monitorService.getMonitor(id));
    }

    @PostMapping("/{id}/pause")
    public ResponseEntity<MonitorResponse> pause(@PathVariable Long id) {
        return ResponseEntity.ok(monitorService.pauseMonitor(id));
    }

    @PostMapping("/{id}/resume")
    public ResponseEntity<MonitorResponse> resume(@PathVariable Long id) {
        return ResponseEntity.ok(monitorService.resumeMonitor(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        monitorService.deleteMonitor(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/checks")
    public ResponseEntity<List<CheckResponse>> getChecks(@PathVariable Long id) {
        return ResponseEntity.ok(checkService.getRecentChecks(id, 50));
    }

    @GetMapping("/{id}/incidents")
    public ResponseEntity<List<IncidentResponse>> getIncidents(@PathVariable Long id) {
        return ResponseEntity.ok(incidentService.getIncidentsForMonitor(id));
    }

    @GetMapping("/{id}/stats")
    public ResponseEntity<StatsResponse> getStats(@PathVariable Long id) {
        return ResponseEntity.ok(monitorService.getStats(id));
    }
}
