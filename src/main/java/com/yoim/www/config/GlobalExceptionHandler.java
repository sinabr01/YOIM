package com.yoim.www.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletResponse;

@Slf4j
@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class GlobalExceptionHandler {

    // 404: 존재하지 않는 URL
    @ExceptionHandler(NoHandlerFoundException.class)
    public String handle404(NoHandlerFoundException ex, Model model, HttpServletResponse res) {
        res.setStatus(HttpStatus.NOT_FOUND.value());
        model.addAttribute("status", 404);
        model.addAttribute("error", "Not Found");
        model.addAttribute("message", "요청하신 페이지를 찾을 수 없습니다.");
        log.warn("GlobalExceptionHandler -> 404 Not Found: {}", ex.getRequestURL());
        return "/yoim/error/nv_error";
    }

    // 405: 메서드 불일치
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public String handle405(HttpRequestMethodNotSupportedException ex, Model model, HttpServletResponse res) {
        res.setStatus(HttpStatus.METHOD_NOT_ALLOWED.value());
        model.addAttribute("status", 405);
        model.addAttribute("error", "Method Not Allowed");
        model.addAttribute("message", "요청 방식이 올바르지 않습니다.");
        log.warn("GlobalExceptionHandler -> 405 Method Not Allowed: {}", ex.getMethod());
        return "/yoim/error/nv_error";
    }

    // 400: Bean Validation 실패 등
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public String handle400(MethodArgumentNotValidException ex, Model model, HttpServletResponse res) {
        res.setStatus(HttpStatus.BAD_REQUEST.value());
        String msg = ex.getBindingResult().getFieldErrors().stream()
                .map(fe -> fe.getField() + ": " + fe.getDefaultMessage())
                .findFirst().orElse("잘못된 요청입니다.");
        model.addAttribute("status", 400);
        model.addAttribute("error", "Bad Request");
        model.addAttribute("message", msg);
        log.warn("GlobalExceptionHandler -> 400 Bad Request: {}", msg);
        return "/yoim/error/nv_error";
    }

    // 500: 기타 예외
    @ExceptionHandler(Exception.class)
    public String handle500(Exception ex, Model model, HttpServletResponse res) {
        res.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        model.addAttribute("status", 500);
        model.addAttribute("error", "Internal Server Error");
        model.addAttribute("message", "문제가 발생했습니다. 잠시 후 다시 시도해주세요.");
        log.error("GlobalExceptionHandler -> 500 Internal Server Error", ex);
        return "/yoim/error/nv_error";
    }
}
