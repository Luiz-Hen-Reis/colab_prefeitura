package com.henr.colab_prefeitura.providers;

import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.henr.colab_prefeitura.modules.users.enums.Role;

@Service
public class JWTUtil {

    @Value("${security.token.secret}")
    private String SECRET_KEY;

    public  String generateToken(String id, Role role) {
        Instant EXPIRATION_TIME = Instant.now().plus(Duration.ofDays(1));
        Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);

        var token = JWT.create().withIssuer("api_cursos")
            .withExpiresAt(EXPIRATION_TIME)
            .withClaim("roles", Arrays.asList(role.getRoleName()))
            .withSubject(id)
            .sign(algorithm);

        return token;
    }

    public String validateToken(String token) {
        Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);

        token = token.replace("Bearer ", "");

        try {
            var subject = JWT.require(algorithm)
                            .build()
                            .verify(token)
                            .getSubject();
            
            return subject;
        } catch (JWTVerificationException e) {
            e.printStackTrace();
            return "";
        }
    }

    public List<String> extractRoles(String token) {
        Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);

        token = token.replace("Bearer ", "");

        try {
            var decodedJWT = JWT.require(algorithm)
                                .build()
                                .verify(token);

            return decodedJWT.getClaim("roles").asList(String.class);
        } catch (JWTVerificationException e) {
            e.printStackTrace();
            return List.of();
        }
    }
}