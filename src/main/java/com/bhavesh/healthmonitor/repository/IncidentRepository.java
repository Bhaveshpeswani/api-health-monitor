package com.bhavesh.healthmonitor.repository;

import com.bhavesh.healthmonitor.entity.Incident;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface IncidentRepository extends JpaRepository<Incident, Long> {
    List<Incident> findByMonitorIdOrderByStartedAtDesc(Long monitorId);
    Optional<Incident> findTopByMonitorIdAndStatusOrderByStartedAtDesc(Long monitorId, String status);
    List<Incident> findByStatus(String status);
}
