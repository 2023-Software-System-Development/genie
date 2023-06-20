package com.example.genie.domain.report.repository;

import com.example.genie.domain.report.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReportRepository extends JpaRepository<Report, Long> {
    @Query(value = "SELECT * FROM report r WHERE r.is_confirmed = false ORDER BY r.created_date asc", nativeQuery = true)
    List<Report> findReportByIsConfirmedFalse();

}
