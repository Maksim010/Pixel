package org.drobysh.pixel;

import org.drobysh.pixel.services.impl.JwtServiceImpl;
import org.drobysh.pixel.utils.JwtAuthFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Configuration
public class TestSecurityConfig {

    @Bean
    @Primary
    public JwtAuthFilter testJwtAuthFilter() {
        return new JwtAuthFilter(mockJwtService());
    }

    @Primary
    @Bean
    public JwtServiceImpl mockJwtService() {
        JwtServiceImpl jwtService = mock(JwtServiceImpl.class);
        when(jwtService.validateToken(anyString())).thenReturn(true);
        when(jwtService.getUserIdFromToken(anyString())).thenReturn(2L);
        return jwtService;
    }
}
