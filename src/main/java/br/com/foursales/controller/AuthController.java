package br.com.foursales.controller;

import br.com.foursales.dto.*;
import br.com.foursales.autentication.services.JwtUtil;
import br.com.foursales.model.UserEntity;
import br.com.foursales.services.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
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

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(userDTO.username().toUpperCase(), userDTO.password()));
            return ResponseFourSales.getResponse(new LoginResponseDTO(jwtUtil.generateToken((User) authentication.getPrincipal())),
                    "Login efetuado com sucesso!",HttpStatus.OK);
        }catch (Exception e ){
            ErrorResponseDTO error = new ErrorResponseDTO("Erro ao efetuar o login do usuário", HttpStatus.NOT_FOUND.name());
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }


    }

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody @Valid UserCreateDTO userDTO) {

        try {

            UserEntity user = new UserEntity(null, userDTO.username(), userDTO.password().toUpperCase(),
                    userDTO.email(),  Role.valueOf(userDTO.role()));

            userService.saveUser(user);

            return ResponseFourSales.getResponse(null, "Usuário cadastrado com sucesso!",
                    HttpStatus.OK);
        }catch (Exception e ){
            ErrorResponseDTO error = new ErrorResponseDTO("Erro ao cadastrar o usuário", HttpStatus.NOT_FOUND.name());
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }

    }
}