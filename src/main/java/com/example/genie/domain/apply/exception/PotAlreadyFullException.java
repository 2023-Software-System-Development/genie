package com.example.genie.domain.apply.exception;

public class PotAlreadyFullException extends RuntimeException{
    public PotAlreadyFullException() {
    }

    public PotAlreadyFullException(String potId) {
        super(potId+"의 인원이 이미 꽉 찼습니다.");
    }
}
