package com.example.genie.domain.pot.entity;

import lombok.Getter;

@Getter
public enum State {
	RECRUITING("모집중"),
	ONGOING("진행중");

	private String description;

	State(String description) {
		this.description = description;
	}
}
