package dev.api.controller;

import dev.api.dto.CreateUserDTO;
import dev.core.domain.User;
import dev.core.service.UserService;
import dev.security.dto.LoginDTO;
import dev.security.dto.TokenDTO;
import dev.security.service.AuthorizationService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

//    @GetMapping("/whoami")
//    @PreAuthorize("isAuthenticated()")
//    public ResponseEntity<UserDTO> whoAmI(Authentication authentication) {
//        MyUserDetails userDetails = (MyUserDetails) authentication.getPrincipal();
//
//        UserDTO userDTO = new UserDTO();
//        userDTO.setId(userDetails.getId());
//        userDTO.setUsername(userDetails.getUsername());
//        userDTO.setRoleName(userDetails.getRoleName());
//
//        return ResponseEntity.ok(userDTO);
//    }
}
