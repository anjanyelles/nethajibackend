package com.nethaji.dto;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserPrincipal {

    private UUID id;

    private String mobileNumber;

    private String email;

    @JsonIgnore
    private String password;

    private Collection<? extends GrantedAuthority> authorities;




    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Collection<? extends GrantedAuthority> authorities) {
        this.authorities = authorities;
    }

    public UserPrincipal(UUID id, String mobileNumber, List<GrantedAuthority> grantedAuths) {
        this.id = id;
        this.mobileNumber = mobileNumber;
        this.authorities = grantedAuths;

    }


    public UserPrincipal(UUID id, String email, String password) {
        this.id = id;
        this.email = email;
        this.password = password;

    }
}


