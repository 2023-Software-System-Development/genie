package com.example.genie.domain.auth.controller;

import com.example.genie.domain.auth.form.UserForm;
import com.example.genie.domain.auth.service.AuthUserService;
import com.example.genie.domain.pot.entity.Pot;
import com.example.genie.domain.pot.form.PotSearchForm;
import com.example.genie.domain.pot.model.PotObject;
import com.example.genie.domain.pot.service.PotService;
import com.example.genie.domain.reliability.Service.ReliabilityService;
import com.example.genie.domain.reliability.entity.Reliability;
import com.example.genie.domain.user.entity.User;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

import javax.validation.Valid;
import java.util.List;

@AllArgsConstructor
@Controller
public class AuthUserController {

    @Autowired
    AuthUserService userAuthUserService;
    PotService potService;
    @RequestMapping("/")
    public String home(Model model, @ModelAttribute PotSearchForm potSearchForm, @PageableDefault(page = 0, size = 8) Pageable pageable, Authentication authentication){
        potSearchForm.setOttType("all");
        Page<PotObject> potList = potService.getPotListBySearch(potSearchForm, pageable);
        model.addAttribute("potList", potList);
        model.addAttribute(potSearchForm);
        return "mainPage/home";
    }
    @GetMapping("/user/login")
    public String loginForm(@ModelAttribute PotSearchForm potSearchForm){
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
