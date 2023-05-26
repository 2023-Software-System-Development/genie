package com.example.genie.domain.auth.controller;

import com.example.genie.domain.auth.service.AuthUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthUserController {
    @Autowired
    AuthUserService userAuthUserService;
    @GetMapping("/user/login")
    public String loginForm(){
        return "login";
    }

}
