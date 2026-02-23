package com.bhavesh.healthmonitor.service;

import com.bhavesh.healthmonitor.dto.CheckResponse;
import com.bhavesh.healthmonitor.entity.Check;
import com.bhavesh.healthmonitor.entity.Monitor;
import com.bhavesh.healthmonitor.repository.CheckRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class CheckService {
    private final CheckRepository checkRepository;
    private final WebClient webClient;

    public CheckService(CheckRepository checkRepository, WebClient.Builder webClientBuilder) {
        this.checkRepository = checkRepository;
        this.webClient = webClientBuilder.build();
    }

    public Check performCheck(Monitor monitor) {
        Check check = new Check();
        check.setMonitorId(monitor.getId());

        long start = System.currentTimeMillis();
        try {
            var response = webClient.get()
                    .uri(monitor.getUrl())
                    .retrieve()
                    .toBodilessEntity()
                    .block(java.time.Duration.ofSeconds(10));

            long elapsed = System.currentTimeMillis() - start;
            check.setResponseTimeMs(elapsed);
            check.setStatusCode(response != null ? response.getStatusCode().value() : null);
            int code = response != null ? response.getStatusCode().value() : 0;
            check.setIsUp(code >= 200 && code < 400);

        } catch (WebClientResponseException ex) {
            long elapsed = System.currentTimeMillis() - start;
            check.setResponseTimeMs(elapsed);
            check.setStatusCode(ex.getStatusCode().value());
            check.setIsUp(false);
        } catch (Exception ex) {
            check.setResponseTimeMs(System.currentTimeMillis() - start);
            check.setStatusCode(null);
            check.setIsUp(false);
        }

        return checkRepository.save(check);
    }

    public List<CheckResponse> getRecentChecks(Long monitorId, int limit) {
        return checkRepository.findByMonitorIdOrderByCheckedAtDesc(monitorId)
                .stream()
                .limit(limit)
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<Check> getChecksAfter(Long monitorId, LocalDateTime after) {
        return checkRepository.findByMonitorIdAndCheckedAtAfter(monitorId, after);
    }

    public java.util.Optional<Check> getLatestCheck(Long monitorId) {
        return checkRepository.findTopByMonitorIdOrderByCheckedAtDesc(monitorId);
    }

    private CheckResponse mapToResponse(Check c) {
        CheckResponse r = new CheckResponse();
        r.setId(c.getId());
        r.setMonitorId(c.getMonitorId());
        r.setStatusCode(c.getStatusCode());
        r.setResponseTimeMs(c.getResponseTimeMs());
        r.setIsUp(c.getIsUp());
        r.setCheckedAt(c.getCheckedAt());
        return r;
    }
}
