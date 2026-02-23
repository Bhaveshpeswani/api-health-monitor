package com.bhavesh.healthmonitor.repository;

import com.bhavesh.healthmonitor.entity.Monitor;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MonitorRepository extends JpaRepository<Monitor, Long> {
    List<Monitor> findByStatus(String status);
}
