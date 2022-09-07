package com.example.profileserver.Security;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
public class WebSecurity {


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(new KeyCloakRoleConverter());


        http.authorizeRequests()
                .antMatchers("/accounts/producer/**").hasRole("producer")
                .antMatchers("/accounts/consumer/**").hasRole("consumer")
                .antMatchers("/accounts/accessToken/**").hasAnyAuthority("SCOPE_profile","SCOPE_deneme") //scope based access ,
                                                                                                        //deneme is a custom scope.
                .anyRequest().authenticated()
                .and()
                .oauth2ResourceServer()
                .jwt()
                .jwtAuthenticationConverter(jwtAuthenticationConverter);
        return http.build();
    }



}
