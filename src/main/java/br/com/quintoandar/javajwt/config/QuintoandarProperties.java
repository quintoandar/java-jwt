package br.com.quintoandar.javajwt.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QuintoandarProperties {

    @Value("${main.url}")
    private String mainUrl;

    public String getMainUrl() {
        return mainUrl;
    }
}
