package com.example.genie.domain.pot.form;

import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
public class PotSearchForm {

    @NotBlank(message = "검색어를 입력해주세요")
    private String searchText;
}
