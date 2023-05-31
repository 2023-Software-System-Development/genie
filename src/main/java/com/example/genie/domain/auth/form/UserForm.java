package com.example.genie.domain.auth.form;

import com.example.genie.domain.auth.service.Role;
import com.example.genie.domain.user.entity.User;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
@Data
public class UserForm {
    @NotBlank(message = "이름을 입력해주세요")
    private String userName;
    @NotBlank(message = "아이디를 입력해주세요")
    private String userLoginId;
    @NotBlank(message = "비밀번호를 입력해주세요")
    @Size(min=8, max = 15, message = "비밀번호는 8~15자로 입렵해주세요")
    private String userPw;
    @NotBlank(message = "비밀번호를 확인해주세요")
    private String userPwCheck;
    @NotBlank(message = "닉네임을 입력해주세요")
    private String userNickName;
    @NotBlank(message = "전화번호를 입력해주세요")
    private String phoneNumber;
    @Email
    private String email;

    @DateTimeFormat
    private LocalDateTime birth;
    private String accountNumber;
    private String bankName;

    public User toEntity(){
        User user = User.builder()
                .userName(userName)
                .userLoginId(userLoginId)
                .userPw(userPw)
                .userNickName(userNickName)
                .phoneNumber(phoneNumber)
                .email(email)
                .birth(birth)
                .accountNumber(accountNumber)
                .bankName(bankName)
                .role(Role.MEMBER) //회원가입으로 생성된 유저는 일단 Member 권한을 받음
                .build();
        return user;
    }
}
