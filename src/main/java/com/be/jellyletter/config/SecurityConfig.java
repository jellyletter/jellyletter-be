package com.be.jellyletter.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // CSRF 보호 비활성화 (실제 환경에서는 신중히 고려해야 합니다)
            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers(
                        "/v3/api-docs/**",
                        "/swagger-ui/**",
                        "/swagger-ui.html",
                        "/swagger-resources/**",
                        "/webjars/**",
                        "/swagger-ui/index.html",
                        "/login/**", // OAuth2 로그인 경로
                        "/oauth2/**", // OAuth2 경로
                        "/public/**", // 공개 API 경로
                        "/static/**", // 정적 자원 경로
                        "/css/**",
                        "/js/**",
                        "/images/**",
//                        "/api/**",
                        "/api/letter/pet**",
                        "/api/pet**",
                        "/api/file**",
                        "/api/info**",
                        "/api/counseling**",
                        "/api/heat**"
                ).permitAll()
                .anyRequest().authenticated()
            )
            .oauth2Login(oauth2 -> oauth2 // OAuth2 로그인 설정
                    .loginPage("/login")
            )
            .formLogin(form -> form
                    .loginPage("/login") // 사용자 정의 로그인 페이지
                    .permitAll()
            )
            .logout(LogoutConfigurer::permitAll
            );

        return http.build();
    }

}
