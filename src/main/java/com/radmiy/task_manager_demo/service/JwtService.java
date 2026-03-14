package com.radmiy.task_manager_demo.service;

import io.jsonwebtoken.Claims;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.function.Function;

public interface JwtService {

    String generateToken(UserDetails userDetails);

    String extractUsername(String token);

    <T> T extractClaim(String token, Function<Claims, T> resolver);

    boolean isTokenValid(String token, UserDetails userDetails);
}
