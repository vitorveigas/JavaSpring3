package com.temqueser.temqueser.services;

import com.temqueser.temqueser.models.Cliente;
import com.temqueser.temqueser.models.Socio;
import com.temqueser.temqueser.models.SocioMaster;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class JwtService {
    @Value("${jwt.secret}")
    private String SECRET_KEY;

    private final long EXPIRATION_TIME = 86400000;

    public String generateToken(Cliente cliente) { //cria o token com o valor da string do id do cliente  expiracao do token
        return Jwts.builder()
                .setSubject(String.valueOf(cliente.getId()))
                .claim("nome",cliente.getNomeCliente())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis()+ EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS256,SECRET_KEY)
                .compact();
    }
    public String generateTokenSocio(Socio socio) {
        return Jwts.builder()
                .setSubject(String.valueOf(socio.getId()))
                .claim("nome", socio.getNomeSocio())
                .claim("tipo", socio.isSocioMaster() ? "master" : "socio")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }
    
    public String generateTokenSocioMaster(SocioMaster socioMaster) {
    return Jwts.builder()
            .setSubject(String.valueOf(socioMaster.getId()))
            .claim("nome", socioMaster.getNomeSocioMaster())
            .claim("tipo", "socio_master") // tipo fixo para identificação
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
            .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
            .compact();
}



    public boolean isTokenValid(String token) { //valida o token
        try{
            extractClaims(token);
            return true;
        }catch(JwtException | IllegalArgumentException e){
            return false;
        }
    }

    public Claims extractClaims(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();
    }

    public Long extractUserId(String token) {
        String subject = extractClaims(token).getSubject();
        return Long.parseLong(subject);
    }

    public String extractTipoUsuario(String token) {
        Claims claims = extractClaims(token);
        return claims.get("tipo", String.class);
    }

}
