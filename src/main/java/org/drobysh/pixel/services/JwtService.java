package org.drobysh.pixel.services;

public interface JwtService {

    String generateToken(Long userId);

    Long getUserIdFromToken(String token);

    boolean validateToken(String token);
}
