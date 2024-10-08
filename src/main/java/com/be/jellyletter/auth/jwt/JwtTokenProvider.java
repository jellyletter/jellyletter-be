package com.be.jellyletter.auth.jwt;

import com.be.jellyletter.auth.dto.TokenDto;
import com.be.jellyletter.enums.Role;
import com.be.jellyletter.model.RefreshToken;
import io.jsonwebtoken.*;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Base64;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    @Value("${jwt.secret-key}")
    private String secretKey;

    @Value("${jwt.refresh-secret-key}")
    private String refreshSecretKey;

    // 액세스 토큰 유효시간 10시간
    private final long accessTokenValidTime = Duration.ofHours(10).toMillis();
    // 리프레시 토큰 유효시간 2주
    private final long refreshTokenValidTime = Duration.ofDays(14).toMillis();


    private final UserDetailsService userDetailsService;
    private final HttpSession session;

    // JWT 토큰 생성
    public TokenDto createToken(Integer userId, Role userRole) {
        Claims claims = Jwts.claims().setSubject(String.valueOf(userId));
        claims.put("userRole", userRole);

        // 새로운 Date 객체 생성
        Date now = new Date();
        Date accessTokenExpiryDate = new Date(now.getTime() + accessTokenValidTime);
        Date refreshTokenExpiryDate = new Date(now.getTime() + refreshTokenValidTime);

        String accessToken = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(accessTokenExpiryDate)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();

        String refreshToken = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(refreshTokenExpiryDate)
                .signWith(SignatureAlgorithm.HS256, refreshSecretKey)
                .compact();

        return TokenDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .keyUserId(String.valueOf(userId))
                .build();
    }

    // JWT 토큰 재생성
    public String recreateAccessToken(String userId, Role userRole) {
        Claims claims = Jwts.claims().setSubject(userId);
        claims.put("userRole", userRole);

        Date now = new Date();
        String accessToken = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + accessTokenValidTime))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();

        return accessToken;
    }

    // JWT 토큰에서 인증 정보 조회
    public Authentication getAuthentication(String token) {
        try {
            String userId = this.getUserId(token);
            System.out.println("***** userid: " + userId);

            UserDetails userDetails = userDetailsService.loadUserByUsername(userId);
            session.setAttribute("principal", userDetails);

            return new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Exception: " + e.getMessage());

            return null;
        }

    }

    // JWT 토큰에서 회원 정보 추출
    public String getUserId(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
    }

    // Request의 Header에서 token 값을 가져옴. "Authorization" : "TOKEN값'
    public String resolveToken(HttpServletRequest request) {
        return request.getHeader("Authorization");
    }

    // 토큰의 유효성 + 만료일자 확인
    public boolean validateToken(String jwtToken) {
        try {
            Jws<Claims> claims = Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(jwtToken);

            // 새로운 Date 객체 생성
            Date now = new Date();
            return !claims.getBody().getExpiration().before(now);
        } catch (SecurityException | MalformedJwtException | UnsupportedJwtException | IllegalArgumentException e) {
            throw new JwtAuthenticationException("TOKEN_INVALID");
        } catch (Exception e) {
            throw new JwtAuthenticationException("TOKEN_EXPIRED");
        }
    }

    // 리프레시 토큰의 유효성 + 만료일자 확인
    public String validateRefreshToken(RefreshToken refreshTokenObj) {
        String refreshToken = refreshTokenObj.getRefreshToken();

        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(refreshSecretKey).parseClaimsJws(refreshToken);

            //refresh 토큰의 만료시간이 지나지 않았을 경우, 새로운 access 토큰을 생성
            if (!claims.getBody().getExpiration().before(new Date())) {
                return recreateAccessToken(claims.getBody().get("sub").toString(), (Role) claims.getBody().get("userRole"));
            }
        }catch (Exception e) {
            //refresh 토큰이 만료되었을 경우, 로그인이 필요.
            return null;

        }

        return null;
    }

    public Long getExpiration(String accessToken) {
        try {
            // accessToken 남은 유효시간
            Date expiration = Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(accessToken).getBody().getExpiration();
            long now = new Date().getTime();

            // accessToken 의 현재 남은시간 반환
            return (expiration.getTime() - now);

        } catch (Exception e) {
            throw new JwtAuthenticationException("TOKEN_EXPIRED");
        }
    }

}
