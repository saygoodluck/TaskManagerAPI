package dev.controller;

import dev.security.MyUserDetails;
import dev.security.dto.LoginDTO;
import dev.security.dto.TokenDTO;
import dev.security.service.AuthorizationService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthorizationService authorizationService;

    public AuthController(AuthorizationService authorizationService) {
        this.authorizationService = authorizationService;
    }

    @PostMapping
    public ResponseEntity<TokenDTO> login(@RequestBody LoginDTO loginDTO) {
        TokenDTO tokenDTO = authorizationService.login(loginDTO);
        return ResponseEntity.ok(tokenDTO);
    }

    @GetMapping("/whoami")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<MyUserDetails> whoAmI(Authentication authentication) {
        return ResponseEntity.ok((MyUserDetails) authentication.getPrincipal());
    }
}
