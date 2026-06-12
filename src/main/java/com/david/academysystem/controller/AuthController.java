package com.david.academysystem.controller;

import com.david.academysystem.dto.auth.cadastro.AuthRequestDTO;
import com.david.academysystem.dto.auth.cadastro.AuthResponseDTO;
import com.david.academysystem.dto.auth.login.LoginRequestDTO;
import com.david.academysystem.dto.token.TokenResponseDTO;
import com.david.academysystem.exception.BadRequestException;
import com.david.academysystem.service.AuthenticationService;
import org.springframework.web.bind.annotation.RequestBody;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/auth")
@RequiredArgsConstructor
@Validated
public class AuthController {

    private final AuthenticationService authenticationService;
    @Value("${jwt.expiration}")
    private long expirationTime;

    @PostMapping("/register/aluno")
    public ResponseEntity<AuthResponseDTO> registerAluno(@RequestBody @Valid AuthRequestDTO dto) throws BadRequestException {
        return ResponseEntity.status(HttpStatus.CREATED).body(authenticationService.registroAluno(dto));
    }

    @PostMapping("/register/funcionario")
    public ResponseEntity<AuthResponseDTO> registerFuncionario(@RequestBody @Valid AuthRequestDTO dto) throws BadRequestException {
        return ResponseEntity.status(HttpStatus.CREATED).body(authenticationService.registroFuncionario(dto));
    }

    @PostMapping("/register/admin")
    public ResponseEntity<AuthResponseDTO> registerAdmin(@RequestBody @Valid AuthRequestDTO dto) throws BadRequestException {
        return ResponseEntity.status(HttpStatus.CREATED).body(authenticationService.registroAdmin(dto));
    }

    @PostMapping("/login")
    public TokenResponseDTO login(@RequestBody @Valid LoginRequestDTO login) throws Exception {
        return authenticationService.login(login);
    }

}
