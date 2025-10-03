package com.akbar.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Map;

public class JwtUtil {


    /**
     * 生成jwt
     * signWith先把secretBase64解码，然后签名
     */
    public static String createJwt(String secret, Long ttlMillis, Map<String, Object> claims) {

        // 过期时间
        long expMills = System.currentTimeMillis() + ttlMillis;
        Date expiration = new Date(expMills);

        // 设置jwt的body
        return Jwts.builder()
                .claims(claims)
                .issuedAt(new Date(System.currentTimeMillis()))     // 签发时间
                .expiration(expiration)
                .signWith(Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret)))
                .compact();
    }


    /**
     * 解析token
     * verifyWith先把secretBase64解码，然后验证token
     */
    public static Claims parseJwt(String secret, String token) {
        return Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret)))
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }


    /**
     * 生成符合要求的随机密钥
     * 然后把密钥复制到application.yml中
     */
    public static String generateRandomKey() {
        SecretKey key = Jwts.SIG.HS256.key().build();
        return Encoders.BASE64.encode(key.getEncoded());    // 转为base64字符串
    }
}
