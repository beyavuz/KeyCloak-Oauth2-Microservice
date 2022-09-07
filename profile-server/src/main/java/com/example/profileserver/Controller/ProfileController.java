package com.example.profileserver.Controller;


import com.example.profileserver.DTO.InfoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/accounts")
public class ProfileController {

    @Autowired
    Environment env;

    @GetMapping("/producer")
    public InfoDTO getProducerInfo(){
        return null;
    }


    @GetMapping("/consumer")
    public InfoDTO getConsumerInfo(){
        return null;
    }

    @GetMapping("/accessToken")
    public Map<String,Object> scopeBasedAccess(@AuthenticationPrincipal Jwt jwt){
        return Collections.singletonMap("Access-Token",jwt);
    }

    @GetMapping("/user/{user_id}")
    @PreAuthorize("hasRole('producer') or #userId==#jwt.subject")
    public String getUser(@PathVariable("user_id") int userId,@AuthenticationPrincipal Jwt jwt){
        return "Success";
    }

    @GetMapping("/port")
    public String getPortNumber(){
        System.out.println("Port no istediler...");
        return env.getProperty("local.server.port");
    }



}
