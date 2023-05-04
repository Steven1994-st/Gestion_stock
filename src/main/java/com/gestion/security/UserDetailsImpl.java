package com.gestion.security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gestion.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Provides core user information for authentification
 */
public class UserDetailsImpl implements UserDetails {

    private static final long serialVersionUID = 1L;
    private String login;

    @JsonIgnore
    private String password;
    private Collection<? extends GrantedAuthority> authorities;

    /**
     * Constructor
     * @param login
     * @param password
     * @param authorities
     */
    public UserDetailsImpl(String login, String password,
                           Collection<? extends GrantedAuthority> authorities) {
        this.login = login;
        this.password = password;
        this.authorities = authorities;
    }

    /**
     * Build user
     * @param user
     * @return
     */
    public static UserDetailsImpl build(User user) {
        Collection<GrantedAuthority> authorities= new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(user.getRole().name()));

        return new UserDetailsImpl(
                user.getLogin(),
                user.getPassword(),
                authorities);
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }
    @Override
    public String getUsername() {
        return login;
    }
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
    @Override
    public boolean isEnabled() {
        return true;
    }
}
