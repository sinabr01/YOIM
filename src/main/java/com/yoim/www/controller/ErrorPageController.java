package com.yoim.www.controller;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ErrorPageController implements ErrorController{
	
	private static final String ERROR_PATH = "/error";

	
	@Override
	public String getErrorPath() {
		// TODO Auto-generated method stub
		return ERROR_PATH;
	}
	
	@RequestMapping("/error")
    public String handleError(HttpServletRequest request) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        HttpStatus httpStatus = HttpStatus.valueOf(Integer.valueOf(status.toString()));
        System.out.println("ErrorPageController -> httpStatus : "+httpStatus.toString());
//        model.addAttribute("code", status.toString());
//        model.addAttribute("msg", httpStatus.getReasonPhrase());
//        model.addAttribute("timestamp", new Date());
        return "/yoim/error/nv_error";
    }

}
