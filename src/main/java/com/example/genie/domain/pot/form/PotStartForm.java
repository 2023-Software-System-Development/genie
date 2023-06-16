package com.example.genie.domain.pot.form;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
@Data
public class PotStartForm {
    @NotBlank(message = "OTT 아이디를 입력해주세요")
    private String ottId;
    @NotBlank(message = "OTT 비밀번호를 입력해주세요")
    private String ottPw;
    @NotBlank(message = "은행을 선택해주세요")
    private String bankName;
    @NotBlank(message = "계좌번호를 입력해주세요")
    private String accountNumber;
    @NotNull(message = "시작일을 입력해주세요")
    private String startDate;
    @NotNull(message = "종료일을 입력해주세요")
    private String endDate;

}
