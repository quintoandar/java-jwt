package br.com.quintoandar.javajwt;

import br.com.quintoandar.javajwt.config.QuintoandarProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QuintoandarJwtConfiguration {

    private final QuintoandarProperties quintoandarProperties;

    @Autowired
    public QuintoandarJwtConfiguration(final QuintoandarProperties quintoandarProperties) {
        this.quintoandarProperties = quintoandarProperties;
    }

    @Bean
    public QuintoAndarJwt quintoAndarJwt() {
        return new QuintoAndarJwtBean(quintoAndarPublicKeyService());
    }

    @Bean
    public QuintoAndarKeycloakJwt quintoAndarKeycloakJwt() {
        return new QuintoAndarKeycloakJwtBean(quintoAndarKeycloakPublicKeyService());
    }

    @Bean
    public QuintoAndarPublicKeyService quintoAndarPublicKeyService() {
        return new QuintoAndarPublicKeyService(quintoandarProperties);
    }

    @Bean
    public QuintoAndarKeycloakPublicKeyService quintoAndarKeycloakPublicKeyService() {
        return new QuintoAndarKeycloakPublicKeyService(quintoandarProperties);
    }

}
