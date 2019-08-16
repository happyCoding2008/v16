package com.qf.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.Test;

import java.util.Date;

/**
 * @author huangguizhao
 */
public class JWTTest {

    @Test
    public void createTokenTest(){
        JwtBuilder builder = Jwts.builder()
                .setId("666").setSubject("行走在牛A的路上")
                .setIssuedAt(new Date())
                //添加自定义属性
                .claim("role","admin")
                .setExpiration(new Date(new Date().getTime()+10))
                .signWith(SignatureAlgorithm.HS256,"huangguizhao");

        String jwtToken = builder.compact();
        System.out.println(jwtToken);
    }

    @Test
    public void checkTokenTest(){
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI2NjYiLCJzdWIiOiLooYzotbDlnKjniZtB55qE6Lev5LiKIiwiaWF0IjoxNTY1OTIwODA1LCJyb2xlIjoiYWRtaW4iLCJleHAiOjE1NjU5MjA4MDV9.Hkn5ZKZQdYwMIbnK6VF1bLuUrByNpvKn";
        Claims claims = Jwts.parser().setSigningKey("huangguizhao")
                .parseClaimsJws(token).getBody();

        System.out.println(claims.getId());
        System.out.println(claims.getSubject());
        System.out.println(claims.getIssuedAt());
        System.out.println(claims.getExpiration());
        //获取属性
        System.out.println(claims.get("role"));
    }
}
