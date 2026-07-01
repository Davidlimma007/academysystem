package com.david.academysystem.controller;

import com.david.academysystem.dto.auth.cadastro.AuthAlunoRequestDTO;
import com.david.academysystem.dto.auth.cadastro.AuthRequestDTO;
import com.david.academysystem.dto.auth.cadastro.AuthResponseDTO;
import com.david.academysystem.dto.auth.login.LoginRequestDTO;
import com.david.academysystem.dto.token.TokenResponseDTO;
import com.david.academysystem.exception.BadRequestException;
import com.david.academysystem.exception.ErrorResponse;
import com.david.academysystem.service.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/auth")
@RequiredArgsConstructor
@Validated
@Tag(name = "Autenticação", description = "Endpoints para registro e login de usuários")
public class AuthController {

    private final AuthenticationService authenticationService;
    @Value("${jwt.expiration}")
    private long expirationTime;

    @Operation(summary = "Cadastrar aluno", description = "Registra um novo usuário com perfil de aluno. Retorna o ID do usuário para uso no endpoint POST /v1/alunos.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Aluno cadastrado com sucesso",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = AuthResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "E-mail já cadastrado ou dados inválidos",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/register/aluno")
    public ResponseEntity<AuthResponseDTO> registerAluno(@RequestBody @Valid AuthAlunoRequestDTO dto) throws BadRequestException {
        return ResponseEntity.status(HttpStatus.CREATED).body(authenticationService.registroAluno(dto));
    }

    @Operation(summary = "Cadastrar funcionário", description = "Registra um novo usuário com perfil de funcionário. Retorna o ID do usuário para uso no endpoint POST /v1/funcionarios.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Funcionário cadastrado com sucesso",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = AuthResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "E-mail já cadastrado ou dados inválidos",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/register/funcionario")
    public ResponseEntity<AuthResponseDTO> registerFuncionario(@RequestBody @Valid AuthRequestDTO dto) throws BadRequestException {
        return ResponseEntity.status(HttpStatus.CREATED).body(authenticationService.registroFuncionario(dto));
    }

    @Operation(summary = "Cadastrar admin", description = "Registra um novo usuário com perfil de administrador.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Admin cadastrado com sucesso",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = AuthResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "E-mail já cadastrado ou dados inválidos",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/register/admin")
    public ResponseEntity<AuthResponseDTO> registerAdmin(@RequestBody @Valid AuthRequestDTO dto) throws BadRequestException {
        return ResponseEntity.status(HttpStatus.CREATED).body(authenticationService.registroAdmin(dto));
    }

    @Operation(summary = "Login", description = "Autentica o usuário e retorna um token JWT para uso nos demais endpoints.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Login realizado com sucesso",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = TokenResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Credenciais inválidas",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/login")
    public TokenResponseDTO login(@RequestBody @Valid LoginRequestDTO login) throws Exception {
        return authenticationService.login(login);
    }
}
