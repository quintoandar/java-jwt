package br.com.quintoandar.javajwt;

import org.jose4j.jwt.consumer.InvalidJwtException;

import java.util.Map;
import java.util.Optional;

public interface QuintoAndarJwt {

    Optional<Map<String, Object>> getPayload(String jwt) throws InvalidJwtException;

}
