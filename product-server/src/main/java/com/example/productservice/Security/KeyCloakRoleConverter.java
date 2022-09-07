package com.example.productservice.Security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class KeyCloakRoleConverter implements Converter<Jwt, Collection<GrantedAuthority>> {

    @Override
    public Collection<GrantedAuthority> convert(Jwt source) {
        var objectMapper = new ObjectMapper();
        Map<?,?> realmAccess = objectMapper.convertValue(source.getClaims().get("realm_access"), Map.class);
        if (realmAccess == null || realmAccess.isEmpty()){
            return new ArrayList<>();
        }
        return ((List<?>)realmAccess.get("roles")).stream()
                .map(roleName -> "ROLE_"+roleName)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }
}
