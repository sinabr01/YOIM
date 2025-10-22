package com.yoim.www.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.yoim.www.model.CustomUserDetails;

@Controller
@RequestMapping("/chat")
public class ChatPageController {

    // ?me=101 같은 식으로 유저 아이디 전달 (임시)
    @GetMapping
    public String chatPage(@RequestParam("to") long to,
    		Authentication authentication, Model model) {
    	CustomUserDetails user = (CustomUserDetails) authentication.getDetails();
    	System.out.println(user.toString());
    	model.addAttribute("to", to);
    	model.addAttribute("me", user.getUserId());
        return "yoim/chat/chat";
    }
}
