package com.quest.bank_card.security;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Collection;

public class UserAuthenticationToken extends AbstractAuthenticationToken {
    private final CustomUserDetails principal;
    private final Jwt jwt;

    public UserAuthenticationToken(Jwt jwt, Collection<GrantedAuthority> auths, CustomUserDetails userDetails) {
        super(auths);
        this.jwt = jwt;
        this.principal = userDetails;
        setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return jwt.getTokenValue();
    }

    @Override
    public Object getPrincipal() {
        return principal;
    }
}
