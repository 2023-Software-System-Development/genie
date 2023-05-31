package com.example.genie.domain.auth.controller;

import com.example.genie.domain.auth.form.UserForm;
import com.example.genie.domain.auth.service.AuthUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

import javax.validation.Valid;

@Controller
public class AuthUserController {

    @Autowired
    AuthUserService userAuthUserService;

    @RequestMapping("/")
    public String home(){
        return "mainPage/home";
    }
    @GetMapping("/user/login")
    public String loginForm(){
        return "user/login";
    }

    @GetMapping("/user/signup")
    public String singUpForm(@ModelAttribute UserForm userForm){
        return "user/signup";
    }

    @PostMapping("/user/signup")
    public String signUp(@Valid @ModelAttribute UserForm userForm, BindingResult bindingResult, SessionStatus sessionStatus){
        if(bindingResult.hasErrors()){
            return "user/signup";
        }
        userAuthUserService.join(userForm, bindingResult);
        if(bindingResult.hasErrors()){
            return "user/signup";
        }

        sessionStatus.setComplete();
        return "redirect:/user/login";
    }
}
