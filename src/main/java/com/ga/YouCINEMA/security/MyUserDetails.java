package com.ga.YouCINEMA.security;

import com.ga.YouCINEMA.enums.UserStatus;
import com.ga.YouCINEMA.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;

public class MyUserDetails implements UserDetails {
    private User user;

    public MyUserDetails(User user){
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities(){
        return new HashSet<>(
                List.of(
                        new SimpleGrantedAuthority(user.getRole().name())
                )
        );
    }

    @Override
    public String getPassword(){
        return user.getPassword();

    }

    @Override
    public String getUsername(){
        return user.getEmail();
    }
    @Override
    public boolean isAccountNonExpired(){
        return true;
    }
    @Override
    public boolean isAccountNonLocked(){
        return true;
    }
    @Override
    public boolean isCredentialsNonExpired(){
        return true;
    }
    @Override
    public boolean isEnabled(){
        return user.isEmailVerified() && user.getStatus() == UserStatus.ACTIVE;
    }


    public User getUser(){
        return user;
    }

}