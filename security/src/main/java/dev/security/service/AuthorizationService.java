package dev.security.service;


import dev.security.dto.TokenDTO;
import dev.security.jwt.JwtService;
import jakarta.transaction.Transactional;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class AuthorizationService {

    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;

    public AuthorizationService(JwtService jwtService, AuthenticationManager authenticationManager) {
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    @Transactional
    public TokenDTO login(String username, String password) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
        authenticationManager.authenticate(authenticationToken);
        var managerJwt = jwtService.generateToken(username);
        return new TokenDTO(managerJwt);
    }
}
