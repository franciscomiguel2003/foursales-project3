package br.com.foursales.controller;

import br.com.foursales.dto.LoginResponseDTO;
import br.com.foursales.dto.UserCreateDTO;
import br.com.foursales.dto.UserDTO;
import br.com.foursales.autentication.services.JwtUtil;
import br.com.foursales.dto.Role;
import br.com.foursales.model.UserEntity;
import br.com.foursales.services.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("auth")
class AuthController {
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserService userService;

    public AuthController(AuthenticationManager authenticationManager, JwtUtil jwtUtil, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userService = userService;
    }


    @PostMapping("/login")
    public ResponseEntity login(@RequestBody @Valid UserDTO userDTO) {

        System.out.println("TESTE LOGIN");
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userDTO.username(), userDTO.password()));
        return ResponseEntity.ok(new LoginResponseDTO(jwtUtil.generateToken((User) authentication.getPrincipal())));

    }

    @PostMapping("/register")
    public UserEntity register(@RequestBody @Valid UserCreateDTO userDTO) {
        UserEntity user = new UserEntity(null, userDTO.username(), userDTO.password(), Role.valueOf(userDTO.role()));
        return userService.saveUser(user);
    }
}