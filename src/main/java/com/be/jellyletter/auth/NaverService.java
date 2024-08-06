package com.be.jellyletter.auth;

import com.be.jellyletter.auth.jwt.JwtService;
import com.be.jellyletter.auth.jwt.JwtTokenProvider;
import com.be.jellyletter.auth.dto.NaverTokenDto;
import com.be.jellyletter.auth.dto.TokenDto;
import com.be.jellyletter.auth.dto.TokenResDto;
import com.be.jellyletter.enums.Oauth2Vendor;
import com.be.jellyletter.enums.Role;
import com.be.jellyletter.model.User;
import com.be.jellyletter.repository.UserRepository;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NaverService {

    @Value("${spring.security.oauth2.client.registration.naver.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.naver.client-secret}")
    private String clientSecret;

    @Value("${spring.security.oauth2.client.provider.naver.token-uri}")
    private String tokenUrl;

    @Value("${spring.security.oauth2.client.provider.naver.user-info-uri}")
    private String userInfoUrl;

    private final JwtTokenProvider jwtTokenProvider;
    private final JwtService jwtService;
    private final UserRepository userRepository;

    public NaverTokenDto getNaverToken(String code, String state) {
        String accessToken = "";
        String refreshToken = "";

        try {
            URL url = new URL(tokenUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            // POST 요청을 위해 기본값이 false 인 setDoOutput 을 true 로 set
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);

            // POST 요청에 필요로 요구하는 파라미터를 스트림을 통해 전송
            try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()))) {
                StringBuilder sb = new StringBuilder();
                sb.append("grant_type=authorization_code")
                        .append("&client_id=").append(clientId)
                        .append("&client_secret=").append(clientSecret)
                        .append("&code=").append(code)
                        .append("&state=").append(state);
                bw.write(sb.toString());
                bw.flush();
            }

            int responseCode = conn.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
                    StringBuilder result = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null) {
                        result.append(line);
                    }

                    // JSON 파싱
                    Gson gson = new Gson();
                    JsonElement element = gson.fromJson(result.toString(), JsonElement.class);
                    accessToken = element.getAsJsonObject().get("access_token").getAsString();
                    refreshToken = element.getAsJsonObject().get("refresh_token").getAsString();
                }
            } else {
                throw new RuntimeException("Failed to get token: HTTP error code : " + responseCode);
            }

        } catch (Exception e) {
            throw new RuntimeException("Failed to get Naver token", e);
        }

        return NaverTokenDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public TokenResDto loginWithNaver(NaverTokenDto naverTokenDto) throws IOException {

        //accessToken 통한 사용자 정보 조회
        try {
            URL url = new URL(userInfoUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            // POST 요청을 위해 기본값이 false 인 setDoOutput 을 true 로 set
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setRequestProperty("Authorization", "Bearer " + naverTokenDto.getAccessToken()); //전송할 header 작성, access_token전송

            //결과 코드가 200이면 성공
            int responseCode = conn.getResponseCode();

            //요청을 통해 얻은 JSON 타입의 Response 메세지 Read
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            StringBuilder result = new StringBuilder();

            while ((line = br.readLine()) != null) {
                result.append(line);
            }

            // JSON 파싱
            JsonElement element = JsonParser.parseString(result.toString());
            String username = element.getAsJsonObject().get("response").getAsJsonObject().get("username").getAsString();
            String nickname = element.getAsJsonObject().get("response").getAsJsonObject().get("nickname").getAsString();
            String email = element.getAsJsonObject().get("response").getAsJsonObject().get("email").getAsString();
            String userPhone = element.getAsJsonObject().get("response").getAsJsonObject().get("userPhone").getAsString();


            if (!userRepository.existsByEmail(email)) {
                // DB에 해당 이메일 없을 경우 회원 가입 로직 실행
                User user = User.builder()
                        .username(username)
                        .userRole(Role.USER)
                        .nickname(nickname)
                        .email(email)
                        .userPhone(userPhone)
                        .vendor(Oauth2Vendor.NAVER)
                        .userStatus(0)
                        .build();

                userRepository.save(user);

                TokenDto tokenDto = jwtTokenProvider.createToken(email, user.getUserRole());
                tokenDto.setGrantType(user.getUserRole());
                jwtService.saveRefreshToken(tokenDto);

                return tokenDto.convertToResDto(user);
            } else {
                // DB에 해당 이메일 회원 정보 있을 경우 jwt token 생성해서 리턴
                Optional<User> user = userRepository.findByEmail(email);

                TokenDto tokenDto = jwtTokenProvider.createToken(email, user.get().getUserRole());
                tokenDto.setGrantType(user.get().getUserRole());
                jwtService.saveRefreshToken(tokenDto);

                return tokenDto.convertToResDto(user.get());
            }

        } catch (Exception e) {
            throw new RuntimeException("Failed to get Naver UserInfo", e);
        }

    }
}
