package com.bhavesh.healthmonitor.repository;

import com.bhavesh.healthmonitor.entity.Check;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


public interface CheckRepository extends JpaRepository<Check, Long> {
    List<Check> findByMonitorIdOrderByCheckedAtDesc(Long monitorId);
    List<Check> findByMonitorIdAndCheckedAtAfter(Long monitorId, LocalDateTime after);
    Optional<Check> findTopByMonitorIdOrderByCheckedAtDesc(Long monitorId);
    List<Check> findByMonitorIdAndIsUpFalseAndCheckedAtAfter(Long monitorId, LocalDateTime after);
}
