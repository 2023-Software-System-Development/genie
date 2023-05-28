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
    @GetMapping("/user/login")
    public String loginForm(){
        return "login";
    }

    @GetMapping("/user/signup")
    public String singUpForm(@ModelAttribute UserForm userForm){
        return "signUp";}

    @PostMapping("/user/signup")
    public String signUp(@Valid @ModelAttribute UserForm userForm, BindingResult bindingResult, SessionStatus sessionStatus){
        if(bindingResult.hasErrors()){
            return "signup";
        }
        userAuthUserService.join(userForm, bindingResult);
        if(bindingResult.hasErrors()){
            return "signup";
        }

        sessionStatus.setComplete();
        return "redirect:/user/login";
    }
}
