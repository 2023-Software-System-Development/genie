package com.example.genie.domain.user.controller;

import com.example.genie.common.util.UserUtils;
import com.example.genie.domain.apply.entity.Apply;
import com.example.genie.domain.apply.service.ApplyService;
import com.example.genie.domain.user.entity.User;
import lombok.AllArgsConstructor;
import org.springframework.boot.Banner;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@AllArgsConstructor
@Controller
@RequestMapping("/user")
public class UserController {

    final UserUtils userUtils;
    final ApplyService applyService;
    @GetMapping("/myPage")
    public String getUserPage(Authentication authentication, Model model){
        User user = userUtils.getUser(authentication);
        model.addAttribute(user);
        return "myPage/mypage";
    }


}
