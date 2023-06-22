package com.example.genie.domain.report.service;

import com.example.genie.domain.reliability.entity.Reliability;
import com.example.genie.domain.reliability.mapper.ReliabilityMapper;
import com.example.genie.domain.reliability.repository.ReliabilityRepository;
import com.example.genie.domain.report.entity.Report;
import com.example.genie.domain.report.model.Type;
import com.example.genie.domain.report.form.ReportForm;
import com.example.genie.domain.report.mapper.ReportMapper;
import com.example.genie.domain.report.model.ReportInfoObject;
import com.example.genie.domain.report.model.ReportObject;
import com.example.genie.domain.report.repository.ReportRepository;
import com.example.genie.domain.user.entity.User;
import com.example.genie.domain.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class ReportService {
    final private ReportRepository reportRepository;
    final private FileService fileService;
    final private UserRepository userRepository;
    final private ReliabilityRepository reliabilityRepository;

    public Report createReport(Long userId, ReportForm reportForm) {
        String fileName=null;
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
        if(user == null)
            throw new EntityNotFoundException();
        Integer userScore = user.getReliabilityScore();
        userScore -= score.getScore();
        user.updateReliability(userScore);
        Report report = reportRepository.findById(reportId).orElseThrow(() -> new EntityNotFoundException("Report not found"));
        report.changeIsConfirmed(true);
        Reliability reliability = ReliabilityMapper.mapToReliabilityWithUser(user, score.getLabel(), score.getScore());
        reliabilityRepository.save(reliability);
    }

    public void rejectReport(Long reportId){
        Report report = reportRepository.findById(reportId).orElseThrow(() -> new EntityNotFoundException("Report not found"));
        report.changeIsConfirmed(true);
        reportRepository.save(report);
    }
    public List<ReportObject> getReportObjectList() {
        List<Report> reports = reportRepository.findReportByIsConfirmedFalse();
        List<ReportObject> reportObjects = reports.stream()
                .map(ReportMapper::toReportObject)
                .collect(Collectors.toList());
        return reportObjects;
    }

    public ReportInfoObject getReport(Long reportId) {
        Report report = reportRepository.findById(reportId).orElseThrow(() -> new EntityNotFoundException("Pot not found"));
        return ReportMapper.toReportInfoObject(report);
    }


}
