package com.example.genie.domain.report.form;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
public class ReportForm {
    @NotNull
    private String userNickName;
    @Min(value = 0) @Max(value = 3)
    private int type;
    private String contents;
    private MultipartFile image;
}
