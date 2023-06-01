package com.example.genie.domain.pot.form;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Setter
@Getter
public class PotSearchForm {

    @NotBlank(message = "검색어를 입력해주세요")
    private String searchText;
}
