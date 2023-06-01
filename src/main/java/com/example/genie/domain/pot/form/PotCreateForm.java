package com.example.genie.domain.pot.form;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class PotCreateForm {
    @NotBlank(message = "팟 이름을 입력해주세요")
    private String potName;
    @NotBlank(message = "ott 정보를 입력해주세요")
    private String ottType;
    @NotNull(message = "가격을 입력해주세요")
    private Integer price;
    @NotNull(message = "모집인원을 입력해주세요")
    private Integer recruit;
    @NotNull(message = "사용기간을 입력해주세요")
    private Integer term;
}
