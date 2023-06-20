package com.example.genie.domain.report.service;

import com.example.genie.domain.reliability.entity.Reliability;
import com.example.genie.domain.reliability.mapper.ReliabilityMapper;
import com.example.genie.domain.reliability.repository.ReliabilityRepository;
import com.example.genie.domain.report.entity.Report;
import com.example.genie.domain.report.entity.Score;
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
                .build();


        return reportRepository.save(report);

    }

    public void reduceReliability(ReportInfoObject reportInfoObject) {
        int type = reportInfoObject.getType();
        Score[] scores = Score.values();
        Score score = scores[type];
        User user = userRepository.findUserByUserNickName(reportInfoObject.getUserNickName());
        Integer userScore = user.getReliabilityScore();
        userScore -= score.getScore();
        user.updateReliability(userScore);
        Report report = reportRepository.findById(reportInfoObject.getReportId()).orElseThrow(() -> new EntityNotFoundException("Pot not found"));
        report.changeIsConfirmed(true);
        Reliability reliability = ReliabilityMapper.mapToReliabilityWithUser(user, score.getLabel(), score.getScore());
        reliabilityRepository.save(reliability);
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
