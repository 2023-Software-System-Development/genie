package com.example.genie.domain.report.entity;

import lombok.Getter;

@Getter
public
enum Score {
    탈주(50), 욕설_비방(10), 잠수(40);

    private final int score;

    Score(int score) {
        this.score = score;
    }

    public int getScore() {
        return this.score;
    }
    public int getIndex() {
        return ordinal();
    }
}
