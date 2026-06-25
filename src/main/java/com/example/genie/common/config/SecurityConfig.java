package com.example.genie.common.config;

import com.example.genie.domain.auth.controller.LoginFailureHandler;
import com.example.genie.domain.auth.service.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Spring Security 5.7+ 컴포넌트 기반 설정.
 * 기존 WebSecurityConfigurerAdapter(@Deprecated)를 SecurityFilterChain/WebSecurityCustomizer 빈으로 대체.
 * UserDetailsService(AuthUserService) + PasswordEncoder 빈은 DaoAuthenticationProvider로 자동 구성된다.
 */
@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    private final LoginFailureHandler loginFailureHandler;

    /** 정적 리소스는 시큐리티 필터 체인에서 제외 */
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring().antMatchers("/css/**", "/js/**", "/image/**");
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // 세션/폼 기반 앱이므로 CSRF 보호는 기본값(활성)을 유지한다. Thymeleaf 폼은 토큰이 자동 주입됨.
        http
                .authorizeRequests() // 인증 절차에 대한 설정
                    .antMatchers("/", "/user/signup", "/user/login", "/loginProc", "/pot/list").permitAll() // 누구나 접근 가능
                    .antMatchers("/report/list", "/report/{reportId}", "/report/{reportId}/confirm", "/report/{reportId}/reject",
                            "/user/userList", "/user/userInfo/**", "/user/addRole").hasRole(Role.ADMIN.toString()) // 관리자 전용
                    .anyRequest().authenticated() // 그 외는 인증 필요
                .and()
                .formLogin()
                    .loginPage("/user/login?req=true") // 접근 차단 시 이동할 url
                    .loginProcessingUrl("/loginProc")  // 로그인 처리 url
                    .defaultSuccessUrl("/pot/list?ottType=all", true)
                    .usernameParameter("userLoginId")
                    .passwordParameter("userPw")
                    .failureHandler(loginFailureHandler)
                    .permitAll()
                .and()
                .logout()
                    .logoutUrl("/user/logout")
                    .logoutSuccessUrl("/")
                    .invalidateHttpSession(true);
        return http.build();
    }
}
