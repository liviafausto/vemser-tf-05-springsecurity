package br.com.dbc.wbhealth.security;

import br.com.dbc.wbhealth.model.entity.UsuarioEntity;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class TokenService {
    private static final String TOKEN_PREFIX = "Bearer";

    @Value("${jwt.expiration}")
    private String expiration;

    @Value("${jwt.secret}")
    private String secret;

    public String generateToken(UsuarioEntity usuarioEntity) {
        Date now = new Date();
        Date exp = new Date(now.getTime() + Long.parseLong(expiration));

        return TOKEN_PREFIX + " " +
                Jwts.builder()
                        .setIssuer("wbhealth-api")
                        .claim(Claims.ID, usuarioEntity.getIdUsuario().toString())
                        .setIssuedAt(now)
                        .setExpiration(exp)
                        .signWith(SignatureAlgorithm.HS256, secret)
                        .compact();
    }

    public UsernamePasswordAuthenticationToken isValid(String token) {
        if (token != null) {
            Claims body = Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token.replace(TOKEN_PREFIX, ""))
                    .getBody();
            String user = body.get(Claims.ID, String.class);
            if (user != null) {
                return new UsernamePasswordAuthenticationToken(user, null, Collections.emptyList());
            }
        }
        return null;
    }
}
