package com.example.genie.domain.pot.form;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class PotCreateForm {
    @NotBlank(message = "팟 이름을 입력해주세요")
    private String potName;
    @NotBlank(message = "ott 정보를 입력해주세요")
    private String ottType;
    @NotBlank(message = "가격을 입력해주세요")
    private Integer price;
    @NotBlank(message = "모집인원을 입력해주세요")
    private Integer recruit;
    @NotBlank(message = "사용기간을 입력해주세요")
    private Integer term;
}
