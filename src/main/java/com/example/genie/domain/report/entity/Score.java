package com.example.genie.domain.report.entity;

import lombok.Getter;

@Getter
public
enum Score {
    탈주("탈주", 50),
    욕설_비방("욕설/비방", 10),
    잠수("잠수", 40);

    private final String label;
    private final int score;

    Score(String label, int score) {
        this.label = label;
        this.score = score;
    }

    public int getScore() {
        return this.score;
    }
    public String getLabel() {return this.label; }
    public int getIndex() {
        return ordinal();
    }
}
