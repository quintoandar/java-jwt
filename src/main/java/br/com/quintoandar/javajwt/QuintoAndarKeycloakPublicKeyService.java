package br.com.quintoandar.javajwt;

import br.com.quintoandar.javajwt.config.QuintoandarProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class QuintoAndarKeycloakPublicKeyService {

    private static final Logger LOGGER = LoggerFactory.getLogger(QuintoAndarKeycloakPublicKeyService.class);

    private static final int BUFFER_SIZE = 8192;

    private final QuintoandarProperties quintoandarProperties;

    @Autowired
    public QuintoAndarKeycloakPublicKeyService(final QuintoandarProperties quintoandarProperties) {
        this.quintoandarProperties = quintoandarProperties;
    }

    protected String fetchKeycloakPublicKey() throws IOException {
        final String JWT_KC_PATH = quintoandarProperties.getKeycloakUrl();

        LOGGER.info("Opening connection to {}", JWT_KC_PATH);
        final URL url = new URL(JWT_KC_PATH);
        final URLConnection connection = url.openConnection();
        connection.connect();

        LOGGER.info("Connection opened, fetching public key");
        final InputStream stream = new BufferedInputStream(url.openStream(), BUFFER_SIZE);

        // Keycloak's realm info endpoint returns a payload with a public_key property
        final ObjectMapper om = new ObjectMapper();
        final Map<String, Object> keycloakRealmInfo = om.readValue(stream, Map.class);

        return (String) keycloakRealmInfo.get("public_key");
    }
}
