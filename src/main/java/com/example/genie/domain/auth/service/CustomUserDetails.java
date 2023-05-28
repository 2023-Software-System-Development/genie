package com.example.genie.domain.auth.service;

import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
/*UserDetails를 구현한 커스텀 UserDetails 클래스*/
@AllArgsConstructor
public class CustomUserDetails implements UserDetails, Serializable {
    private Long id; //DB의 PK
    private String userName;
    private String userLoginId;
    private String userPw;
    private String userNickName;
    private String phoneNumber;
    private String email;
    private LocalDateTime birth;
    private String accountNumber;
    private String bankName;
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        if(id.equals("admin")){
            authorities.add(new SimpleGrantedAuthority(Role.ADMIN.getValue()));
        }else{
            authorities.add(new SimpleGrantedAuthority(Role.MEMBER.getValue()));
        }
        return authorities;
    }

    @Override
    public String getPassword() {
        return userPw;
    }

    @Override
    public String getUsername() {
        return userLoginId;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
