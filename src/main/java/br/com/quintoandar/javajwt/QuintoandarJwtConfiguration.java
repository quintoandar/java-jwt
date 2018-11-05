package br.com.quintoandar.javajwt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import br.com.quintoandar.javajwt.config.QuintoandarProperties;

@Configuration
public class QuintoandarJwtConfiguration {

    private QuintoandarProperties quintoandarProperties;

    @Autowired
    public QuintoandarJwtConfiguration(QuintoandarProperties quintoandarProperties) {
        this.quintoandarProperties = quintoandarProperties;
    }

    @Bean
    public QuintoAndarJwt quintoAndarJwt() {
        return new QuintoAndarJwtBean(quintoAndarPublicKeyService());
    }

    @Bean
    public QuintoAndarPublicKeyService quintoAndarPublicKeyService() {
        return new QuintoAndarPublicKeyService(quintoandarProperties);
    }

}
