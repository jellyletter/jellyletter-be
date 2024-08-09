package com.be.jellyletter.config;

import com.be.jellyletter.auth.jwt.JwtAuthenticationEntryPoint;
import com.be.jellyletter.auth.jwt.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;  // JwtAuthenticationEntryPoint 추가

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // CSRF 보호 비활성화
//            .cors(cors -> cors.configurationSource(webConfig.corsConfigurationSource())) // CORS 설정
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
                                "/api/letter**",
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
                .logout(logout -> logout
                        .permitAll()
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // 세션 사용하지 않음
                )
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(jwtAuthenticationEntryPoint)  // JwtAuthenticationEntryPoint 등록
                );

        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
