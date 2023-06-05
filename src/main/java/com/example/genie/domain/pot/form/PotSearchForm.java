package com.example.genie.domain.pot.form;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Setter
@Getter
public class PotSearchForm {

    private String ottType; //현재 가져올 리스트의 ottType
    private String searchType; //검색 종류: potName: 팟 이름, remain: 남은 인원, term: 기간
    //@NotBlank(message = "검색어를 입력해주세요")
    private String searchText;
}
