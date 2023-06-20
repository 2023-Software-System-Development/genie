package com.example.genie.domain.report.controller;

import com.example.genie.common.util.UserUtils;
import com.example.genie.domain.report.entity.Report;
import com.example.genie.domain.report.form.ReportForm;
import com.example.genie.domain.report.model.ReportInfoObject;
import com.example.genie.domain.report.model.ReportObject;
import com.example.genie.domain.report.service.ReportService;
import com.example.genie.domain.user.entity.User;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@AllArgsConstructor
@RequestMapping("/report")
public class ReportController {
    final private ReportService reportService;
    final private UserUtils userUtils;

    @ModelAttribute("reportType")
    public Map<Integer, String> reportType(){
        Map<Integer, String> reportType = new HashMap<>();
        reportType.put(0, "탈주");
        reportType.put(1, "욕설/비방");
        reportType.put(2, "잠수");
        reportType.put(3, "기타");
        return reportType;
    }
    @GetMapping
    public String reportForm(@ModelAttribute ReportForm reportForm){
        return "/report/report";
    }

    @PostMapping
    public String createReport(@Validated @ModelAttribute ReportForm reportForm, BindingResult bindingResult, Authentication authentication, RedirectAttributes redirectAttributes){
        User user = userUtils.getUser(authentication);
        Report report = reportService.createReport(user.getId(), reportForm);
        if(report==null){
            bindingResult.reject("ReportError");
            return "/report/report";
        }

        redirectAttributes.addFlashAttribute("success", true);
        return "redirect:/report";
    }


    //신고 확인 후 신뢰도 깎기
    @PostMapping("/confirm")
    public void reduceReliability(@ModelAttribute ReportInfoObject reportInfoObject) {
        reportService.reduceReliability(reportInfoObject);
    }

    //유저들의 신고 내역 확인
    @GetMapping("/list")
    public String getReportList(Model model) {
        List<ReportObject> reportObjectList = reportService.getReportObjectList();
        model.addAttribute("reportList", reportObjectList);
        return "report/home";
    }

    @GetMapping("/{reportId}")
    public String getReport(@PathVariable Long reportId, Model model) {
        ReportInfoObject reportInfoObject = reportService.getReport(reportId);
        return "report/reportInfo";
    }


}
