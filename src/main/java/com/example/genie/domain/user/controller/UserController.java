package com.example.genie.domain.user.controller;

import com.example.genie.domain.pot.dto.PotDto;
import com.example.genie.domain.user.dto.UserInfo;
import com.example.genie.domain.user.entity.User;
import com.example.genie.domain.user.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@Controller
@RequestMapping("/user")
public class UserController {

    final UserService userService;

    @GetMapping("/info")
    public String getUserPage(Authentication authentication, Model model){
        User user = userService.findLoginUser(authentication);
        model.addAttribute(user);
        return "myPage/mypage";
    }

    @GetMapping("/joinPotList")
    public String getUserJoinPotList(Authentication authentication, Model model, @PageableDefault(page = 1, size = 6) Pageable pageable){
        Page<PotDto> userApplyPotList = userService.getUserApplyPotList(authentication, pageable);
        model.addAttribute("userApplyPotList", userApplyPotList);

        return "myPage/mypot_joinList";
    }

    @GetMapping("/potList")
    public String getUserPotList(Authentication authentication, Model model, @PageableDefault(page = 1, size = 6) Pageable pageable){
        Page<PotDto> userPotList = userService.getUserPotList(authentication, pageable);
        model.addAttribute("userPotList", userPotList);

        return "myPage/mypot_list";
    }

    @GetMapping("/userList")
    public String getUserList(Model model){
       List<UserInfo> userList = userService.getAllUser();
       model.addAttribute("userList", userList);
       return "system/userList";
    }

    @GetMapping("/userInfo/{userId}")
    public String getUserInfo(@PathVariable Long userId, Model model){
        UserInfo user = userService.getUserInfo(userId);
        model.addAttribute("user", user);
        return "system/userInfo";
    }

    @PostMapping("/addRole")
    public String addRole(@RequestParam Long userId){
        userService.addRole(userId);
        return "redirect:/user/userList";
    }
}
