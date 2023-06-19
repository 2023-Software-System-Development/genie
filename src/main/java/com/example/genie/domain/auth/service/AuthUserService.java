package com.example.genie.domain.auth.service;

import com.example.genie.domain.auth.form.UserForm;
import com.example.genie.domain.reliability.Service.ReliabilityService;
import com.example.genie.domain.reliability.entity.Reliability;
import com.example.genie.domain.reliability.model.ReliabilityInfoObject;
import com.example.genie.domain.reliability.repository.ReliabilityRepository;
import com.example.genie.domain.user.entity.User;
import com.example.genie.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import javax.transaction.Transactional;

/*유저 정보 인증에 사용되는 서비스*/
@RequiredArgsConstructor
@Service
public class AuthUserService implements org.springframework.security.core.userdetails.UserDetailsService {

    @Autowired
    private final UserRepository userRepository;
    private final ReliabilityService reliabilityService;

    @Override
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
        }
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String encodedUserPw = bCryptPasswordEncoder.encode(userForm.getUserPw());
        userForm.setUserPw(encodedUserPw);
        User user = userRepository.save(userForm.toEntity()); //새로운 회원 생성
        reliabilityService.addReliability(user, new ReliabilityInfoObject("회원가입", 100));
    }
}
