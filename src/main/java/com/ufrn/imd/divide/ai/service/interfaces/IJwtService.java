package com.ufrn.imd.divide.ai.service.interfaces;

import com.ufrn.imd.divide.ai.model.UserDetailsImpl;
import io.jsonwebtoken.Claims;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.function.Function;

public interface IJwtService {
    Long extractUserIdFromRequest();

    boolean isValid(String token, UserDetails user);

    String extractUsername(String token);

    <T> T extractClaim(String token, Function<Claims, T> resolver);

    String generateToken(UserDetailsImpl user);
}
