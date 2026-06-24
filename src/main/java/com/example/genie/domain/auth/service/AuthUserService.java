package com.example.genie.domain.auth.service;

import com.example.genie.domain.auth.form.UserForm;
import com.example.genie.domain.reliability.service.ReliabilityService;
import com.example.genie.domain.reliability.model.ReliabilityInfoObject;
import com.example.genie.domain.user.entity.User;
import com.example.genie.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

/*유저 정보 인증에 사용되는 서비스*/
@RequiredArgsConstructor
@Service
public class AuthUserService implements org.springframework.security.core.userdetails.UserDetailsService {

    private final UserRepository userRepository;
    private final ReliabilityService reliabilityService;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        User user = userRepository.findUserByUserLoginId(userId);

        if(user == null){
            throw new UsernameNotFoundException("해당 사용자가 존재하지 않습니다.");
        }

        return new CustomUserDetails(user.getId(), user.getUserName(), user.getUserLoginId(),
        user.getUserPw(), user.getUserNickName(), user.getPhoneNumber(), user.getEmail(), user.getBirth(), user.getRole());
    }

    @Transactional
    public void join(UserForm userForm, BindingResult bindingResult){
        //password 중복 검사
        if(!userForm.getUserPw().equals(userForm.getUserPwCheck())){
            bindingResult.rejectValue("userPwCheck", "passwordMismatch", "패스워드가 다릅니다.");
            return;
        }
        //id 중복 검사
        if(userRepository.findUserByUserLoginId(userForm.getUserLoginId())!=null){
            bindingResult.rejectValue("userLoginId", "ExistMember", "이미 존재하는 아이디입니다");
            return;
        }
        String encodedUserPw = passwordEncoder.encode(userForm.getUserPw());
        userForm.setUserPw(encodedUserPw);
        User user = userRepository.save(userForm.toEntity()); //새로운 회원 생성
        reliabilityService.addReliability(user, new ReliabilityInfoObject("회원가입", 50));
    }
}
