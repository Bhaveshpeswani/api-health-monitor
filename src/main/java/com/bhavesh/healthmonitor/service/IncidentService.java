package com.bhavesh.healthmonitor.service;

import com.bhavesh.healthmonitor.dto.IncidentResponse;
import com.bhavesh.healthmonitor.entity.Check;
import com.bhavesh.healthmonitor.entity.Incident;
import com.bhavesh.healthmonitor.entity.Monitor;
import com.bhavesh.healthmonitor.repository.IncidentRepository;
import com.bhavesh.healthmonitor.repository.MonitorRepository;
import org.springframework.stereotype.Service;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class IncidentService {

    private final IncidentRepository incidentRepository;
    private final MonitorRepository monitorRepository;

    public IncidentService(IncidentRepository incidentRepository,
                           MonitorRepository monitorRepository) {
        this.incidentRepository = incidentRepository;
        this.monitorRepository = monitorRepository;
    }

    public void processCheckResult(Monitor monitor, Check check) {
        Optional<Incident> openIncident = incidentRepository
                .findTopByMonitorIdAndStatusOrderByStartedAtDesc(monitor.getId(), "OPEN");

        if (!check.getIsUp()) {
            // Monitor is DOWN
            if (openIncident.isEmpty()) {
                // No open incident exists — create one
                Incident incident = new Incident();
                incident.setMonitorId(monitor.getId());
                incidentRepository.save(incident);
            }
            // Update monitor health status
            monitor.setHealthStatus("DOWN");
            monitorRepository.save(monitor);

        } else {
            // Monitor is UP
            if (openIncident.isPresent()) {
                // There was an open incident — resolve it
                Incident incident = openIncident.get();
                incident.setResolvedAt(LocalDateTime.now());
                incident.setStatus("RESOLVED");
                long duration = Duration.between(incident.getStartedAt(), LocalDateTime.now()).getSeconds();
                incident.setDurationSeconds(duration);
                incidentRepository.save(incident);
            }

            // Check if degraded (had failures in last 10 minutes)
            LocalDateTime tenMinutesAgo = LocalDateTime.now().minusMinutes(10);
            boolean recentFailures = monitor.getHealthStatus().equals("DOWN");
            monitor.setHealthStatus(recentFailures ? "DEGRADED" : "HEALTHY");
            monitorRepository.save(monitor);
        }
    }

    public List<IncidentResponse> getIncidentsForMonitor(Long monitorId) {
        return incidentRepository.findByMonitorIdOrderByStartedAtDesc(monitorId)
                .stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    public List<IncidentResponse> getAllOpenIncidents() {
        return incidentRepository.findByStatus("OPEN")
                .stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    private IncidentResponse mapToResponse(Incident i) {
        IncidentResponse r = new IncidentResponse();
        r.setId(i.getId());
        r.setMonitorId(i.getMonitorId());
        r.setStartedAt(i.getStartedAt());
        r.setResolvedAt(i.getResolvedAt());
        r.setDurationSeconds(i.getDurationSeconds());
        r.setStatus(i.getStatus());
        return r;
    }
}
