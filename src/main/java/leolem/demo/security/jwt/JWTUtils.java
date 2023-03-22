package leolem.demo.security.jwt;

import java.util.Calendar;
import java.util.Date;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.*;

import leolem.demo.users.data.User;
import lombok.val;

@Component
public class JWTUtils {

  @Value("{leolem.app.jwtSecret}")
  private String jwtSecret;

  private int jwtExpiration = 86400;

  public String generateJWTToken(Authentication authentication) {

    val user = (User) authentication.getPrincipal();

    val issuance = new Date();
    val calendar = Calendar.getInstance();
    calendar.setTime(issuance);
    calendar.add(Calendar.SECOND, jwtExpiration);
    val expiration = calendar.getTime();

    return Jwts.builder()
        .setSubject(user.getUsername())
        .setIssuedAt(issuance)
        .setExpiration(expiration)
        .signWith(SignatureAlgorithm.HS512, jwtSecret)
        .compact();
  }

  public String getEmailFromJwtToken(String token) {
    return Jwts.parser()
        .setSigningKey(jwtSecret)
        .parseClaimsJws(token)
        .getBody()
        .getSubject();
  }

  public boolean validateJwtToken(String token) {
    try {
      Jwts.parser()
          .setSigningKey(jwtSecret)
          .parseClaimsJws(token);

      return true;
    } catch (SignatureException e) {
      System.err.println("Invalid JWT signature: " + e.getMessage());
    } catch (MalformedJwtException e) {
      System.err.println("Invalid JWT token:" + e.getMessage());
    } catch (ExpiredJwtException e) {
      System.err.println("JWT token is expired:" + e.getMessage());
    } catch (UnsupportedJwtException e) {
      System.err.println("JWT token is unsupported: " + e.getMessage());
    } catch (IllegalArgumentException e) {
      System.err.println("JWT claims string is empty: " + e.getMessage());
    }

    return false;
  }

}
