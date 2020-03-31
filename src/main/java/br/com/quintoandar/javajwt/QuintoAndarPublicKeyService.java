package br.com.quintoandar.javajwt;

import br.com.quintoandar.javajwt.config.QuintoandarProperties;
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

public class QuintoAndarPublicKeyService {

    private static final Logger LOGGER = LoggerFactory.getLogger(QuintoAndarPublicKeyService.class);

    private static final int BUFFER_SIZE = 8192;

    private static final String AUTH_ENDPOINT = "/auth/key";

    private final QuintoandarProperties quintoandarProperties;

    @Autowired
    public QuintoAndarPublicKeyService(final QuintoandarProperties quintoandarProperties) {
        this.quintoandarProperties = quintoandarProperties;
    }

    // visible for testing
    protected String fetchMainPublicKey() throws IOException {
        final String JWT_MAIN_PATH = quintoandarProperties.getMainUrl() + AUTH_ENDPOINT;
        LOGGER.info("Opening connection to {}", JWT_MAIN_PATH);
        final URL url = new URL(JWT_MAIN_PATH);
        final URLConnection connection = url.openConnection();
        connection.connect();

        LOGGER.info("Connection opened, fetching public key");

        final InputStream stream = new BufferedInputStream(url.openStream(), BUFFER_SIZE);
        return IOUtils.toString(stream, StandardCharsets.UTF_8);
    }

}
