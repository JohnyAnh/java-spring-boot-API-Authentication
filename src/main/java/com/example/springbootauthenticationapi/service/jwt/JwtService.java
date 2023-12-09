package com.example.springbootauthenticationapi.service.jwt;

import com.example.springbootauthenticationapi.service.UserDetail;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class JwtService {
    private static final String secret_key = "1234567890";
    private static final Long expired_time = 1234567832L;

    public String generateToken(Authentication authentication) {
        UserDetail userDetail = (UserDetail) authentication.getPrincipal();

        // Đảm bảo 'expired_time' được định nghĩa và hợp lệ
        long expired_time = 300/* thời gian hết hạn của token tính bằng giây */;

        // Tạo khóa ký có sử dụng thuật toán HS512
        SecretKey secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS512);

        return Jwts.builder()
                .setSubject(userDetail.getUsername())
                .setExpiration(new Date(new Date().getTime() + expired_time * 1000))
                .setIssuedAt(new Date())
                .signWith(secretKey, SignatureAlgorithm.HS512)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(secret_key).parseClaimsJws(token);
            return true;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return false;
    }

    public String getUsernameFromToken(String token) {
        return  Jwts.parser().setSigningKey(secret_key).parseClaimsJws(token).getBody().getSubject();
    }


}
