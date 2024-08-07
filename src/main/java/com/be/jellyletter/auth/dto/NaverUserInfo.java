package com.be.jellyletter.auth.dto;

import java.util.Map;

public class NaverUserInfo extends OAuth2UserInfo {

    public NaverUserInfo(Map<String, Object> attributes) {
        super(attributes);
    }

    Map<String,Object> naverAccount = (Map<String, Object>)attributes.get("naver_account");
    Map<String, Object> profile = (Map<String, Object>) naverAccount.get("profile");

    @Override
    public String getUsername() {
        return (String) naverAccount.get("name");
    }

    @Override
    public String getNickname() {
        return (String) naverAccount.get("nickname");
    }

    @Override
    public String getEmail() {
        return (String) naverAccount.get("email");
    }

    @Override
    public String getUserPhone() {
        return (String) naverAccount.get("mobile");
    }
}
