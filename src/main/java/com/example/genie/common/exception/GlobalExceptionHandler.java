package com.example.genie.common.exception;

import lombok.extern.log4j.Log4j2;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.persistence.EntityNotFoundException;

/**
 * 컨트롤러 전반에서 발생하는 예외를 공통 에러 페이지로 처리한다.
 * (인가 실패(AccessDeniedException)는 Spring Security가 403으로 처리)
 */
@Log4j2
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public String handleCustomException(CustomException e, Model model) {
        model.addAttribute("errorMessage", e.getMessage());
        return "error";
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public String handleEntityNotFound(EntityNotFoundException e, Model model) {
        log.warn("Entity not found: {}", e.getMessage());
        model.addAttribute("errorMessage", "요청하신 데이터를 찾을 수 없습니다.");
        return "error";
    }
}
