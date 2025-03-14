package br.com.foursales.controller;

import br.com.foursales.DTO.UserDTO;
import br.com.foursales.autentication.services.JwtUtil;
import br.com.foursales.model.ProdutoEntity;
import br.com.foursales.model.Role;
import br.com.foursales.model.UserEntity;
import br.com.foursales.services.ProdutoService;
import br.com.foursales.services.UserService;
import jakarta.validation.Valid;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;
import java.util.List;

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
    public String login(@RequestBody @Valid UserDTO userDTO) {

        System.out.println("TESTE LOGIN");
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userDTO.username(), userDTO.password()));
        return jwtUtil.generateToken(authentication.getName(), ((User) authentication.getPrincipal()).getAuthorities());

    }

    @PostMapping("/register")
    public UserEntity register(@RequestParam String username, @RequestParam String password, @RequestParam Role role) {
        return userService.saveUser(username, password, role);
    }
}