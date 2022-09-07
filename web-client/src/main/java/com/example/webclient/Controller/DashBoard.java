package com.example.webclient.Controller;


import com.example.webclient.ModelDTO.ProductDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Controller
public class DashBoard {

    @Autowired
    OAuth2AuthorizedClientService oAuth2AuthorizedClientService;

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    WebClient webClient;

    @GetMapping("/dashboard")
    public String getDashBoard(Model model, @AuthenticationPrincipal OidcUser oidcUser){
        var oauthTokenObject = (OAuth2AuthenticationToken)SecurityContextHolder.getContext().getAuthentication();

        var oauth2Client = oAuth2AuthorizedClientService.
                loadAuthorizedClient(oauthTokenObject.getAuthorizedClientRegistrationId(), oauthTokenObject.getName());

        var accessToken = oauth2Client.getAccessToken().getTokenValue();
        model.addAttribute("access_token",accessToken);

        var idToken = oidcUser.getIdToken().getTokenValue();
        model.addAttribute("id_token",idToken);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization","Bearer "+accessToken);
        var products = this.getProducts(headers);
        model.addAttribute("products",products);

        var productServicePort = this.getProductServicePort();
        model.addAttribute("product_service_port",productServicePort);

        var profileServicePort = this.getProfileServicePort();
        model.addAttribute("profile_service_port",profileServicePort);

        return "dashboard";
    }

    private List<ProductDTO> getProducts(HttpHeaders headers){
        HttpEntity<List<ProductDTO>> requestEntity = new HttpEntity<>(headers);
        var responseEntity = restTemplate.exchange("http://localhost:8080/product/all",
                HttpMethod.GET,
                requestEntity,
                new ParameterizedTypeReference<List<ProductDTO>>() {
                });
        return responseEntity.getBody();
    }

    private String getProductServicePort(){
        return webClient.get()
                .uri("http://localhost:8080/product/port")
                .retrieve().bodyToMono(new ParameterizedTypeReference<String>() {})
                .block();
    }

    private String getProfileServicePort(){
        return webClient.get()
                .uri("http://localhost:8080/accounts/port")
                .retrieve().bodyToMono(new ParameterizedTypeReference<String>() {})
                .block();
    }



}
