package com.nethaji.service;



import com.nethaji.dto.UserPrincipal;
import com.nethaji.entity.User;
import com.nethaji.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CustomAuthenticationManager {

    @Autowired
    private CustomPasswordEncoder customPasswordEncoder;

    @Autowired
    private UserRepository repository;


    public Authentication authenticateByEmail(Authentication authentication) {
        String credentials = authentication.getPrincipal() + "";

        System.out.println("credentials :" + credentials);

        String password = authentication.getCredentials() + "";

        Optional<User> userOptional = repository.findByEmail(credentials);

        User user = userOptional.orElseThrow(() -> new BadCredentialsException("1000"));

        List<GrantedAuthority> grantedAuths = new ArrayList<>();
        grantedAuths.add(new SimpleGrantedAuthority("ROLE_USER"));

        UserPrincipal userPrincipal = new UserPrincipal(user.getId(), user.getEmail(), grantedAuths);

        return new UsernamePasswordAuthenticationToken(userPrincipal, password, grantedAuths);
    }

}






