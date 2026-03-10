package com.yoim.www.model;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.Data;

@Data
public class CustomUserDetails implements UserDetails{
	private Long userId;
    private String loginId;
    private String username;
    private String password;
    private String nickname;
    private String email;
    private String userType;
    private String userImg;
    private String birthDate;
    private String gender;
    private String intro;
    private String interests;
    private String phone;
    private String providerId;
    private String providerType;
    private String userDelYn;
    private String userDelDate;
    private Long userImgId;
    private String registId;
    private String registDt;
    private String updusrId;
    private String updtDt;
    private boolean isEnabled;
    private boolean isAccountNonExpired;
    private boolean isAccountNonLocked;
    private boolean isCredentialsNonExpired;
    private Collection<? extends GrantedAuthority> authorities;

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }

    @Override
    public boolean isAccountNonExpired() {
        return isAccountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return isAccountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return isCredentialsNonExpired;
    }
}
