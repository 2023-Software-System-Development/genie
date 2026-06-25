package com.example.genie.domain.report.service;

import com.example.genie.domain.reliability.entity.Reliability;
import com.example.genie.domain.reliability.mapper.ReliabilityMapper;
import com.example.genie.domain.reliability.repository.ReliabilityRepository;
import com.example.genie.domain.report.entity.Report;
import com.example.genie.domain.report.dto.Type;
import com.example.genie.domain.report.form.ReportForm;
import com.example.genie.domain.report.mapper.ReportMapper;
import com.example.genie.domain.report.dto.ReportInfoDto;
import com.example.genie.domain.report.dto.ReportDto;
import com.example.genie.domain.report.repository.ReportRepository;
import com.example.genie.domain.user.entity.User;
import com.example.genie.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ReportService {
    private final ReportRepository reportRepository;
    private final FileService fileService;
    private final UserRepository userRepository;
    private final ReliabilityRepository reliabilityRepository;

    @Transactional
    public Report createReport(Long userId, ReportForm reportForm) {
        String fileName = null;
        if (reportForm.getImage() != null && !reportForm.getImage().isEmpty())
            fileName = fileService.fileUpload(reportForm.getImage());
        Report report = Report.builder()
                .userId(userId)
                .type(reportForm.getType())
                .userNickName(reportForm.getUserNickName())
                .imageUrl(fileName)
                .contents(reportForm.getContents())
                .isConfirmed(false)
                .build();

        return reportRepository.save(report);
    }

    @Transactional
    public void confirmReport(Long reportId, Integer type, String userNickName) {
        Type score = Type.getType(type);
        User user = userRepository.findUserByUserNickName(userNickName);
        if (user == null)
            throw new EntityNotFoundException("신고 대상 사용자를 찾을 수 없습니다.");
        Integer userScore = user.getReliabilityScore();
        userScore -= score.getScore();
        user.updateReliability(userScore);
        Report report = reportRepository.findById(reportId).orElseThrow(() -> new EntityNotFoundException("Report not found"));
        report.changeIsConfirmed(true);
        // 신뢰도 원장에는 실제 변동값(차감이므로 음수)을 기록해 부호를 일치시킴
        Reliability reliability = ReliabilityMapper.mapToReliabilityWithUser(user, score.getLabel(), -score.getScore());
        reliabilityRepository.save(reliability);
    }

    @Transactional
    public void rejectReport(Long reportId) {
        Report report = reportRepository.findById(reportId).orElseThrow(() -> new EntityNotFoundException("Report not found"));
        report.changeIsConfirmed(true);
        reportRepository.save(report);
    }

    @Transactional(readOnly = true)
    public List<ReportDto> getReportObjectList() {
        List<Report> reports = reportRepository.findReportByIsConfirmedFalse();
        return reports.stream()
                .map(ReportMapper::toReportObject)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ReportInfoDto getReport(Long reportId) {
        Report report = reportRepository.findById(reportId).orElseThrow(() -> new EntityNotFoundException("Report not found"));
        return ReportMapper.toReportInfoObject(report);
    }

}
