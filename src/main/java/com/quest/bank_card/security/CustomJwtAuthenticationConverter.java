package com.quest.bank_card.security;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class CustomJwtAuthenticationConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    @Override
    public AbstractAuthenticationToken convert(Jwt jwt) {

        List<String> roles = jwt.getClaimAsStringList("authorities");
        if (roles == null) {
            roles = List.of();
        }

        Collection<GrantedAuthority> authorities = roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                .collect(Collectors.toList());

        String login = jwt.getClaimAsString("login");
        UUID id = UUID.fromString(jwt.getSubject());

        CustomUserDetails principal = new CustomUserDetails();
        principal.setId(id);
        principal.setLogin(login);
        principal.setAuthorities(authorities);

        return new UserAuthenticationToken(jwt, authorities, principal);
    }
}
