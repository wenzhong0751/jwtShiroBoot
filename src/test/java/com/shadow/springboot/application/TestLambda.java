package com.shadow.springboot.application;

import io.jsonwebtoken.*;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.validation.constraints.Null;
import javax.xml.bind.DatatypeConverter;
import java.util.*;

public class TestLambda {
    public static final String SECRET_KEY = "?::4343fdf4fdf6cvf):";

    public static void main(String[] args) {
//        List<String> list = Arrays.asList("xuxiaoxiao", "xudada", "xuzhongzhong");
//        list.forEach(System.out::println);
        TestLambda testLambda = new TestLambda();
        String token = testLambda.createJWT();
        token = "eyJhbGciOiJIUzUxMiIsInppcCI6IkRFRiJ9.eNokzFEOwiAQBNC77DckAgtCL2O2dDWopaaAMTHeXdDPeZmZN1xrggkwGrYOWZJHkkhMMvigpYnezBHd2S4KBJQ293LlUlvhPdPKHVMpA7cbZ9n1yftAqjApe1QqaIdWAL8ef9Dq8IN9u_MY0rKmLEY6XVp_hs8XAAD__w.4-v_Kr3QDbyZ-Zh1pTywptvSNOg6K9Cra5PBVnud_eCwpUiZaacD88qFnd1sMWcxJ4pmG2N09pzCVLkyNyLjQQ";
        System.out.println(token);
        Claims c = testLambda.parseJWT(token);
        System.out.println(c.getId());
    }

    private String createJWT(){
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        Map<String,Object> claims = new HashMap<String, Object>();
        claims.put("uid","aaaaaa");
        claims.put("user_name","admin");
        claims.put("nick_name","sunwz");
        SecretKey key = generalKey();
        JwtBuilder builder = Jwts.builder()
                .setClaims(claims)
                .setId("testID")
                .setIssuedAt(now)
                .setSubject("subjecttest")
                .signWith(signatureAlgorithm,key);
        return builder.compact();
    }

    public Claims parseJWT(String jwt){
        SecretKey key = generalKey();
        byte[] secreKeyBytes = DatatypeConverter.parseBase64Binary(SECRET_KEY);
        JwsHeader claims = Jwts.parser().setSigningKey(secreKeyBytes).parseClaimsJws(jwt).getHeader();
        System.out.println(claims.toString());
        Claims claims1 = Jwts.parser().setSigningKey(secreKeyBytes).parseClaimsJws(jwt).getBody();
        return claims1;
    }

    private SecretKey generalKey(){
        String stringKey = "7786df7fc3a34e26a61c034d5ec8245d";
        byte[] encodedKey = Base64.decodeBase64(stringKey);
        SecretKey key = new SecretKeySpec(encodedKey,0,encodedKey.length,"AES");
        return key;
    }
}
