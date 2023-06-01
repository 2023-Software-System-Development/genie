package com.example.genie;

import com.example.genie.domain.auth.controller.LoginAuthHandler;
import com.example.genie.domain.auth.service.AuthUserService;
import com.example.genie.domain.auth.service.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Autowired
    AuthUserService customUserDetailsService;


    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/css/**", "/js/**", "/image/**");
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(customUserDetailsService);
    }
    @Override
    protected void configure(HttpSecurity http) throws Exception {
            http.csrf().disable()// 세션을 사용하지 않고 JWT 토큰을 활용하여 진행, csrf토큰검사를 비활성화
                .authorizeRequests() // 인증절차에 대한 설정을 진행
                .antMatchers("/", "/user/signup", "/user/login", "/loginProc").permitAll() // 설정된 url은 인증되지 않더라도 누구든 접근 가능
                .anyRequest().authenticated()// 위 페이지 외 인증이 되어야 접근가능(ROLE에 상관없이)
                .and()
                .formLogin().loginPage("/user/login")  // 접근이 차단된 페이지 클릭시 이동할 url
                .loginProcessingUrl("/loginProc") // 로그인시 맵핑되는 url
                .defaultSuccessUrl("/", true)
                .usernameParameter("userLoginId")      // view form 태그 내에 로그인 할 id 에 맵핑되는 name ( form 의 name )
                .passwordParameter("userPw")      // view form 태그 내에 로그인 할 password 에 맵핑되는 name ( form 의 name )
                //.successHandler(new LoginAuthHandler()) // 로그인 성공시 실행되는 메소드
                //.failureHandler(failureHandlerHandler()) // 로그인 실패시 실행되는 메소드
                .permitAll()
                .and()
                .logout() // 로그아웃 설정
                .logoutUrl("/user/logout") // 로그아웃시 맵핑되는 url
                .logoutSuccessUrl("/") // 로그아웃 성공시 리다이렉트 주소
                .invalidateHttpSession(true); // 세션 clear
    }
}