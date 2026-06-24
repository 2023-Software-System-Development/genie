package com.example.genie.domain.report.form;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
public class ReportForm {
    @NotNull
    private String userNickName;
    // Type enum은 0~2 (탈주/욕설_비방/잠수)
    @Min(value = 0) @Max(value = 2)
    private int type;
    private String contents;
    private MultipartFile image;
}
