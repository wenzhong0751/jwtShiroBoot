package com.shadow.springboot.application;

import io.jsonwebtoken.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.xml.bind.DatatypeConverter;

@RunWith(SpringRunner.class)
@SpringBootTest
public class JJWTTest {
    public static final String SECRET_KEY = "?::4343fdf4fdf6cvf):";

    @Test
    public void testParse(){
        String token = "eyJhbGciOiJIUzUxMiIsInppcCI6IkRFRiJ9.eNoszF0OwiAQBOC77DMkQPmzlzFrXQ1qqWHBmJjeXTA-zpeZ-cCtJpjhEimSVyinELy0GKKMflqkcai8C_pgyIMAbqdersS1MZWMK3VMzAO3O2XZ9UVlIFaYdV9qZa3TAuj9_IMxPyjbg8YQz2vKYqTjtfVn2L8AAAD__w.vKLiETo0AM7SOj-0QydPcld3Y1SDperZ0OnVfIVyi0PYpCIjY5R4dZ6cr4nYuYHFmTk_f28gk4wT4FbjesVcag";
        byte[] secreKeyBytes = DatatypeConverter.parseBase64Binary(SECRET_KEY);
        JwtParser jwtParser = Jwts.parser().setSigningKey(secreKeyBytes);
        Jwt<Header, Claims> claimsJwt = jwtParser.parseClaimsJwt(token);
        Header header = claimsJwt.getHeader();
        Claims claims = claimsJwt.getBody();
        String temp = Jwts.parser().setSigningKey(secreKeyBytes).parseClaimsJwt(token).getHeader().toString();
        System.out.println(temp);
    }
}
