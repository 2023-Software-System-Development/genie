package com.example.genie.domain.report.model;

import lombok.Getter;

@Getter
public
enum Type {
    탈주("탈주", 50),
    욕설_비방("욕설/비방", 10),
    잠수("잠수", 40);

    private final String label;
    private final Integer score;

    Type(String label, Integer score) {
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
    public static Type getType(Integer i){
        return Type.values()[i];
    }
}
