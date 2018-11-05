package br.com.quintoandar.javajwt;

import java.io.IOException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Map;
import java.util.Optional;
import org.jose4j.jwk.RsaJsonWebKey;
import org.jose4j.jwt.consumer.InvalidJwtException;
import org.jose4j.jwt.consumer.JwtConsumer;
import org.jose4j.jwt.consumer.JwtConsumerBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class QuintoAndarJwt {

  private final static Logger logger = LoggerFactory.getLogger(QuintoAndarJwt.class);

  private final static String KEY_ALGORITHM = "RSA";

  private QuintoAndarPublicKeyService quintoAndarPublicKeyService;

  private JwtConsumer jwtConsumer;

  public QuintoAndarJwt() {
    quintoAndarPublicKeyService = new QuintoAndarPublicKeyService();
  }

  public Optional<Map<String, Object>> getPayload(String jwt) throws InvalidJwtException {
    if (jwt == null) {
      return Optional.empty();
    }

    if (!isReady()) {
      try {
        setup();
      } catch (SetupException e) {
        logger.error("Failed to setup QuintoAndarJwt", e);
        return Optional.empty();
      }
    }

    Map<String, Object> payload = jwtConsumer.processToClaims(jwt).getClaimsMap();
    return Optional.ofNullable(payload);
  }

  private void setup() throws SetupException {
    try {
      logger.info("Setting up QuintoAndarJwt");
      quintoAndarPublicKeyService.setup();
      KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
      X509EncodedKeySpec keySpec = getPublicKeySpec();
      RSAPublicKey publicKey = (RSAPublicKey) keyFactory.generatePublic(keySpec);
      RsaJsonWebKey webKey = new RsaJsonWebKey(publicKey);
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

  private String strippedKey(String publicKey) {
    return publicKey.replaceAll("-----(BEGIN|END) PUBLIC KEY-----", "").replaceAll("\\n", "");
  }

  // visible for testing
  protected void setQuintoAndarPublicKeyService(QuintoAndarPublicKeyService quintoAndarPublicKeyService) {
    this.quintoAndarPublicKeyService = quintoAndarPublicKeyService;
  }

}
