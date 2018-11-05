package br.com.quintoandar.javajwt;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Properties;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class QuintoAndarPublicKeyService {

    private final static Logger logger = LoggerFactory.getLogger(QuintoAndarPublicKeyService.class);

    private final static int BUFFER_SIZE = 8192;

    private final static String UTF_8 = "UTF-8";

    private String JWT_MAIN_PATH;

    private Properties appProperties = new Properties();

    protected void setup() throws IOException {
        String propertiesFileName =
                new StringBuilder(Thread.currentThread().getContextClassLoader().getResource("").getPath()).append(
                        "application.properties").toString();
        appProperties.load(new FileInputStream(propertiesFileName));
        JWT_MAIN_PATH = (String) appProperties.get("main.url");
    }

    // visible for testing
    protected String fetchMainPublicKey() throws IOException {
        logger.info("Opening connection to {}", JWT_MAIN_PATH);
        URL url = new URL(JWT_MAIN_PATH);
        URLConnection connection = url.openConnection();
        connection.connect();

        logger.info("Connection opened, fetching public key");

        InputStream stream = new BufferedInputStream(url.openStream(), BUFFER_SIZE);
        return IOUtils.toString(stream, UTF_8);
    }

}
