package com.wanted.global.jwt;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Base64;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class JwtTokenProvider {
    private SecretKey cachedSecretKey;

    @Value("${custom.jwt.secretKey}")
    private String secretKdyPlain;

    @Value("${custom.jwt.accessTime}")
    private int accessTime;

    private SecretKey _getSecretKey(){
        String keyBase64Encoded = Base64.getEncoder().encodeToString(secretKdyPlain.getBytes());
        return Keys.hmacShaKeyFor(keyBase64Encoded.getBytes());
    }

    public SecretKey getSecretKey() {
        if (cachedSecretKey == null) cachedSecretKey = _getSecretKey();
        return cachedSecretKey;
    }

    public String generateToken(Map<String, Object> claims) {
        Date accessTokenExpireDate = Date.from(Instant.now().plusSeconds(accessTime)); // 토큰 만료일 설정

        return Jwts.builder()
                .signWith(getSecretKey(), SignatureAlgorithm.HS512) // 시그니처 key값
                .claim("body", Json.toStr(claims)) // 클레임
                .setIssuedAt(new Date()) // 생성일
                .setExpiration(accessTokenExpireDate) // 만료일
                .compact();
    }

    private static class Json {

        public static String toStr(Object obj) {
            try {
                return new ObjectMapper().writeValueAsString(obj);
            } catch (JsonProcessingException e) {
                return null;
            }
        }

        public static Map<String, Object> toMap(String jsonStr) {
            try {
                return new ObjectMapper().readValue(jsonStr, LinkedHashMap.class);
            } catch (JsonProcessingException e) {
                return Collections.emptyMap();
            }
        }
    }
}
