package com.example.genie.domain.auth.service;

import com.example.genie.domain.user.entity.User;
import com.example.genie.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/*유저 정보 인증에 사용되는 서비스*/
@RequiredArgsConstructor
@Service
public class AuthUserService implements org.springframework.security.core.userdetails.UserDetailsService {

    private final UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        User user = UserRepository.findByUserLoginId(userId);

        if(user == null){
            throw new UsernameNotFoundException("해당 사용자가 존재하지 않습니다.");
        }

        return new CustomUserDetails(user.getId(), user.getMemberName(), user.getMemberLoginId(),
        user.getMemberPw(), user.getMemberNickName(), user.getPhoneNumber(), user.getEmail(), user.getBirth(), user.getAccountNumber(), user.getBankName());
    }

}
