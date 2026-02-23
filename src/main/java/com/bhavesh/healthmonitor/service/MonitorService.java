package com.bhavesh.healthmonitor.service;

import com.bhavesh.healthmonitor.dto.*;
import com.bhavesh.healthmonitor.entity.Check;
import com.bhavesh.healthmonitor.entity.Monitor;
import com.bhavesh.healthmonitor.exception.ResourceNotFoundException;
import com.bhavesh.healthmonitor.repository.CheckRepository;
import com.bhavesh.healthmonitor.repository.IncidentRepository;
import com.bhavesh.healthmonitor.repository.MonitorRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MonitorService {

    private final MonitorRepository monitorRepository;
    private final CheckRepository checkRepository;
    private final IncidentRepository incidentRepository;

    public MonitorService(MonitorRepository monitorRepository,
                          CheckRepository checkRepository,
                          IncidentRepository incidentRepository) {
        this.monitorRepository = monitorRepository;
        this.checkRepository = checkRepository;
        this.incidentRepository = incidentRepository;
    }


    public MonitorResponse createMonitor(MonitorRequest request) {
        if (request.getName() == null || request.getName().isBlank())
            throw new IllegalArgumentException("Monitor name is required");
        if (request.getUrl() == null || request.getUrl().isBlank())
            throw new IllegalArgumentException("URL is required");
        if (!request.getUrl().startsWith("http"))
            throw new IllegalArgumentException("URL must start with http or https");

        Monitor monitor = new Monitor();
        monitor.setName(request.getName());
        monitor.setUrl(request.getUrl());
        monitor.setCheckIntervalSeconds(
                request.getCheckIntervalSeconds() != null ? request.getCheckIntervalSeconds() : 60
        );
        return mapToResponse(monitorRepository.save(monitor));
    }

    public List<MonitorResponse> getAllMonitors() {
        return monitorRepository.findAll().stream()
                .map(this::mapToResponse).collect(Collectors.toList());
    }

    public MonitorResponse getMonitor(Long id) {
        return mapToResponse(monitorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Monitor not found: " + id)));
    }

    public MonitorResponse pauseMonitor(Long id) {
        Monitor m = monitorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Monitor not found: " + id));
        m.setStatus("PAUSED");
        return mapToResponse(monitorRepository.save(m));
    }

    public MonitorResponse resumeMonitor(Long id) {
        Monitor m = monitorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Monitor not found: " + id));
        m.setStatus("ACTIVE");
        return mapToResponse(monitorRepository.save(m));
    }

    public void deleteMonitor(Long id) {
        if (!monitorRepository.existsById(id))
            throw new ResourceNotFoundException("Monitor not found: " + id);
        monitorRepository.deleteById(id);
    }

    public StatsResponse getStats(Long monitorId) {
        Monitor monitor = monitorRepository.findById(monitorId)
                .orElseThrow(() -> new ResourceNotFoundException("Monitor not found: " + monitorId));

        LocalDateTime since = LocalDateTime.now().minusHours(24);
        List<Check> checks = checkRepository.findByMonitorIdAndCheckedAtAfter(monitorId, since);

        long total = checks.size();
        long up = checks.stream().filter(Check::getIsUp).count();
        long down = total - up;
        double uptime = total > 0 ? (up * 100.0 / total) : 100.0;
        double avgResponse = checks.stream()
                .filter(c -> c.getResponseTimeMs() != null)
                .mapToLong(Check::getResponseTimeMs)
                .average().orElse(0.0);

        long totalIncidents = incidentRepository.findByMonitorIdOrderByStartedAtDesc(monitorId).size();
        long openIncidents = incidentRepository.findByStatus("OPEN").stream()
                .filter(i -> i.getMonitorId().equals(monitorId)).count();

        String lastChecked = checkRepository.findTopByMonitorIdOrderByCheckedAtDesc(monitorId)
                .map(c -> c.getCheckedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .orElse("Never");

        StatsResponse stats = new StatsResponse();
        stats.setMonitorId(monitorId);
        stats.setMonitorName(monitor.getName());
        stats.setHealthStatus(monitor.getHealthStatus());
        stats.setTotalChecks(total);
        stats.setUpChecks(up);
        stats.setDownChecks(down);
        stats.setUptimePercentage(Math.round(uptime * 100.0) / 100.0);
        stats.setAverageResponseTimeMs(Math.round(avgResponse * 100.0) / 100.0);
        stats.setTotalIncidents(totalIncidents);
        stats.setOpenIncidents(openIncidents);
        stats.setLastCheckedAt(lastChecked);
        return stats;
    }

    private MonitorResponse mapToResponse(Monitor m) {
        MonitorResponse r = new MonitorResponse();
        r.setId(m.getId());
        r.setName(m.getName());
        r.setUrl(m.getUrl());
        r.setCheckIntervalSeconds(m.getCheckIntervalSeconds());
        r.setStatus(m.getStatus());
        r.setHealthStatus(m.getHealthStatus());
        r.setCreatedAt(m.getCreatedAt());
        return r;
    }
}
