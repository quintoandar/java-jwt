package br.com.quintoandar.javajwt.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QuintoandarProperties {

    @Value("${main.url}")
    private String mainUrl;

    @Value("${keycloak.url}")
    private String keycloakUrl;

    public String getMainUrl() {
        return mainUrl;
    }

    public String getKeycloakUrl() {
        return keycloakUrl;
    }
}
