package com.itheima.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;
import java.util.Map;

/**
 * JWT 令牌工具类
 * 用于生成和解析 JWT 令牌
 */
public class JwtUtils {

    // 签名密钥（Base64编码）
    private static final String SECRET_KEY = "aXRoZWltYQ==";
    // 令牌过期时间：12小时
    private static final long EXPIRATION_TIME = 12 * 60 * 60 * 1000;

    /**
     * 生成 JWT 令牌
     *
     * @param claims 令牌中携带的数据（如 id, username）
     * @return 生成的 JWT 字符串
     */
    public static String generateToken(Map<String, Object> claims) {
        return Jwts.builder()
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .addClaims(claims)
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .compact();
    }

    /**
     * 解析 JWT 令牌
     *
     * @param token JWT 令牌字符串
     * @return 令牌中的 Claims 数据
     * @throws Exception 如果令牌无效或已过期
     */
    public static Claims parseToken(String token) throws Exception {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();
    }
}
