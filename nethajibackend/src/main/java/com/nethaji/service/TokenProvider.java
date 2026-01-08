package com.nethaji.service;




import com.nethaji.dto.UserPrincipal;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.SignatureException;
import java.util.Date;
import java.util.UUID;

@Service
public class TokenProvider {

    // private static final Logger logger =
    // LoggerFactory.getLogger(TokenProvider.class);
    @Autowired
    private final AppProperties appProperties;

    public TokenProvider(AppProperties appProperties) {
        this.appProperties = appProperties;
    }

//	public String createToken(Authentication authentication) {
//		UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
//
//		Date now = new Date();
//		Date expiryDate = new Date(now.getTime() + appProperties.getAuth().getTokenExpirationMSec());
//
//		return Jwts.builder().setSubject(userPrincipal.getId().toString()).setIssuedAt(new Date())
//				.setExpiration(expiryDate).signWith(SignatureAlgorithm.HS512, appProperties.getAuth().getTokenSecret())
//				.compact();
//	}

    public String createToken(org.springframework.security.core.Authentication authentication, String type) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        Integer expirationTimeLong;
        System.out.print("type: " + type);
        /*
         * if ("ACCESS".equals(type)) { expirationTimeLong =
         * appProperties.getAuth().getTokenExpirationMSec(); } else { expirationTimeLong
         * = appProperties.getAuth().getRefreshTokenExpiration(); }
         */

        if ("ACCESS".equals(type)) {
            expirationTimeLong = appProperties.getAuth().getTokenExpirationMSec() != null
                    ? appProperties.getAuth().getTokenExpirationMSec()
                    : 300000; // Default to 5 minutes
        } else {
            expirationTimeLong = appProperties.getAuth().getRefreshTokenExpiration() != null
                    ? appProperties.getAuth().getRefreshTokenExpiration()
                    : 1800000; // Default to 30 minutes
        }

        Date now = new Date();
        Date expiryDate = null;
        try {
            expiryDate = new Date(now.getTime() + expirationTimeLong);
            System.out.println("Expiry Date: " + expiryDate);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Ensure token secret is not null or empty
        //String tokenSecret = appProperties.getAuth().getTokenSecret();
        String tokenSecret="04ca023b39512e46d0c2cf4b48d5aac61d34302994c87ed4eff225dcf3b0a218739f3897051a057f9b846a69ea2927a587044164b7bae5e1306219d50b588cb1";
        System.out.println("tokenSecret: " + tokenSecret);

        if (tokenSecret == null || tokenSecret.isEmpty()) {
            throw new RuntimeException("Token secret not configured");
        }

        return Jwts.builder().setSubject(userPrincipal.getId().toString()) // Set user ID as subject
                .setIssuedAt(now) // Set the current time as issued time
                .setExpiration(expiryDate) // Set expiration time
                .signWith(SignatureAlgorithm.HS512, tokenSecret) // Sign the token with the secret key
                .compact(); // Return the compact JWT token
    }
    public UUID getUserIdFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(appProperties.getAuth().getTokenSecret())
                .parseClaimsJws(token)
                .getBody();

        // Convert the subject to Integer if it's stored as a user ID
        return UUID.fromString(claims.getSubject());
    }


    public boolean validateToken(String authToken) throws SignatureException {
        try {
            System.out.println("validate token=" + authToken);
            Jwts.parser().setSigningKey(appProperties.getAuth().getTokenSecret()).parseClaimsJws(authToken);
            return true;
        } catch (MalformedJwtException ex) {
            // logger.error("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            // logger.error("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            // logger.error("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            // logger.error("JWT claims string is empty.");
        }
        return false;
    }

}








