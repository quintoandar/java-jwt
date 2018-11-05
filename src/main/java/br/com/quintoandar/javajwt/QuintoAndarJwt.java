package br.com.quintoandar.javajwt;

import java.util.Map;
import java.util.Optional;

import org.jose4j.jwt.consumer.InvalidJwtException;

public interface QuintoAndarJwt {

    Optional<Map<String, Object>> getPayload(String jwt) throws InvalidJwtException;

}
