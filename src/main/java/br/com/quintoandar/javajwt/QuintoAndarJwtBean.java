package br.com.quintoandar.javajwt;

import org.jose4j.jwk.RsaJsonWebKey;
import org.jose4j.jwt.consumer.InvalidJwtException;
import org.jose4j.jwt.consumer.JwtConsumer;
import org.jose4j.jwt.consumer.JwtConsumerBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Map;
import java.util.Optional;

public class QuintoAndarJwtBean implements QuintoAndarJwt {

    private static final Logger LOGGER = LoggerFactory.getLogger(QuintoAndarJwtBean.class);

    private static final String KEY_ALGORITHM = "RSA";

    private QuintoAndarPublicKeyService quintoAndarPublicKeyService;

    private JwtConsumer jwtConsumer;

    @Autowired
    public QuintoAndarJwtBean(final QuintoAndarPublicKeyService quintoAndarPublicKeyService) {
        this.quintoAndarPublicKeyService = quintoAndarPublicKeyService;
    }

    @Override
    public Optional<Map<String, Object>> getPayload(final String jwt) throws InvalidJwtException {
        if (jwt == null) {
            return Optional.empty();
        }

        if (!isReady()) {
            try {
                setup();
            } catch (SetupException e) {
                LOGGER.error("Failed to setup QuintoAndarJwtBean", e);
                return Optional.empty();
            }
        }

        final Map<String, Object> payload = jwtConsumer.processToClaims(jwt).getClaimsMap();
        return Optional.ofNullable(payload);
    }

    private void setup() throws SetupException {
        try {
            LOGGER.info("Setting up QuintoAndarJwtBean");
            final KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
            final X509EncodedKeySpec keySpec = getPublicKeySpec();
            final RSAPublicKey publicKey = (RSAPublicKey) keyFactory.generatePublic(keySpec);
            final RsaJsonWebKey webKey = new RsaJsonWebKey(publicKey);
            jwtConsumer = new JwtConsumerBuilder().setVerificationKey(webKey.getKey()).build();
        } catch (NoSuchAlgorithmException | IOException | InvalidKeySpecException e) {
            throw new SetupException(e);
        }
    }

    private boolean isReady() {
        return jwtConsumer != null;
    }

    private X509EncodedKeySpec getPublicKeySpec() throws IOException {
        final String encodedKey = strippedKey(quintoAndarPublicKeyService.fetchMainPublicKey());

        final byte[] decodedKey = Base64.getDecoder().decode(encodedKey);

        return new X509EncodedKeySpec(decodedKey);
    }

    private String strippedKey(final String publicKey) {
        return publicKey.replaceAll("-----(BEGIN|END) PUBLIC KEY-----", "")
                        .replaceAll("\\n", "");
    }

    // visible for testing
    protected void setQuintoAndarPublicKeyService(final QuintoAndarPublicKeyService quintoAndarPublicKeyService) {
        this.quintoAndarPublicKeyService = quintoAndarPublicKeyService;
    }

}
