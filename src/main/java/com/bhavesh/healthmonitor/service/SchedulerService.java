package com.bhavesh.healthmonitor.service;

import com.bhavesh.healthmonitor.entity.Check;
import com.bhavesh.healthmonitor.entity.Monitor;
import com.bhavesh.healthmonitor.repository.MonitorRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.util.List;


@Service
public class SchedulerService {


    private final MonitorRepository monitorRepository;
    private final CheckService checkService;
    private final IncidentService incidentService;

    public SchedulerService(MonitorRepository monitorRepository,
                            CheckService checkService,
                            IncidentService incidentService) {
        this.monitorRepository = monitorRepository;
        this.checkService = checkService;
        this.incidentService = incidentService;
    }

    // This Runs every 60 seconds
    @Scheduled(fixedDelay = 60000)
    public void runChecks() {
        List<Monitor> activeMonitors = monitorRepository.findByStatus("ACTIVE");
        System.out.println("[Scheduler] Running checks for " + activeMonitors.size() + " monitors");

        for (Monitor monitor : activeMonitors) {
            try {
                Check result = checkService.performCheck(monitor);
                incidentService.processCheckResult(monitor, result);
                System.out.println("[Scheduler] " + monitor.getName() +
                        " -> " + (result.getIsUp() ? "UP" : "DOWN") +
                        " (" + result.getResponseTimeMs() + "ms)");
            } catch (Exception e) {
                System.err.println("[Scheduler] Error checking " + monitor.getName() + ": " + e.getMessage());
            }
        }
    }
}
