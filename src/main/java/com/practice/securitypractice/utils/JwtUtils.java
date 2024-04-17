package com.practice.securitypractice.utils;

import java.util.Date;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;

@Component
public class JwtUtils {

    @Value("${security.jwt.privateKey}")
    private String privateKey;

    @Value("${security.jwt.user}")
    private String userGenerator;

    public String createJWT(Authentication authentication) {

        Algorithm algorithm = Algorithm.HMAC256(this.privateKey);

        String JwtToken = JWT.create()
        .withIssuer(this.userGenerator)
        .withSubject(authentication.getPrincipal().toString())
        .withClaim("authorities", authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(",")))
        .withIssuedAt(new Date())
        .withExpiresAt(new Date(System.currentTimeMillis() + 86400000))
        .withJWTId(UUID.randomUUID().toString())
        .withNotBefore(new Date(System.currentTimeMillis()))
        .sign(algorithm);

        return JwtToken;
    }

    public DecodedJWT validateJwt(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(this.privateKey);

            JWTVerifier verifier = JWT.require(algorithm)
            .withIssuer(userGenerator)
            .build();

            return verifier.verify(token);
        } catch (JWTVerificationException e) {
            throw new JWTVerificationException("Token is not valid");
        }
    }

    public String extractUsername(DecodedJWT decodedJWT) {
        return decodedJWT.getSubject().toString();
    }

    public Claim extractSpecifyClaim(DecodedJWT decodedJWT, String claim) {
        return decodedJWT.getClaim(claim);
    }
}
