package dev.api.controller;

import dev.core.domain.User;
import dev.core.dto.CreateUserDTO;
import dev.core.service.UserService;
import dev.api.dto.MyUserDetailsDTO;
import dev.security.MyUserDetails;
import dev.security.dto.LoginDTO;
import dev.security.dto.TokenDTO;
import dev.security.service.AuthorizationService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthorizationService authorizationService;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public AuthController(AuthorizationService authorizationService, UserService userService, PasswordEncoder passwordEncoder) {
        this.authorizationService = authorizationService;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    public ResponseEntity<TokenDTO> login(@RequestBody LoginDTO loginDTO) {
        TokenDTO tokenDTO = authorizationService.login(loginDTO.getUsername().trim(), loginDTO.getPassword().trim());
        return ResponseEntity.ok(tokenDTO);
    }

    @PostMapping("/register")
    public ResponseEntity<TokenDTO> register(@RequestBody CreateUserDTO createUserDTO) {
        String encodedPassword = passwordEncoder.encode(createUserDTO.getPassword().trim());
        User user = userService.create(createUserDTO.getUsername().trim(), encodedPassword);
        if (user != null) {
            TokenDTO tokenDTO = authorizationService.login(createUserDTO.getUsername().trim(), createUserDTO.getPassword().trim());
            return ResponseEntity.ok(tokenDTO);
        }
        return ResponseEntity.badRequest().build();
    }

    @GetMapping("/whoami")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<MyUserDetailsDTO> whoAmI(Authentication authentication) {
        MyUserDetails userDetails = (MyUserDetails) authentication.getPrincipal();

        MyUserDetailsDTO userDetailsDTO = new MyUserDetailsDTO();
        userDetailsDTO.setId(userDetails.getId());
        userDetailsDTO.setUsername(userDetails.getUsername());
        userDetailsDTO.setRoleName(userDetails.getRoleName());

        return ResponseEntity.ok(userDetailsDTO);
    }
}
