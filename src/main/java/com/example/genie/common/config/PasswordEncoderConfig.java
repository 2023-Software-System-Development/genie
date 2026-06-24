package com.example.genie.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * PasswordEncoder 빈을 SecurityConfig에서 분리.
 * (SecurityConfig가 AuthUserService를 주입받고, AuthUserService가 PasswordEncoder를
 *  주입받으므로, 같은 클래스에 두면 순환 참조가 발생한다.)
 */
@Configuration
public class PasswordEncoderConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
