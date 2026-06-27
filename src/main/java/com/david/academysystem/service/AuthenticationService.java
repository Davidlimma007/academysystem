package com.david.academysystem.service;

import com.david.academysystem.config.TokenProvider;
import com.david.academysystem.database.model.Aluno;
import com.david.academysystem.database.model.Role;
import com.david.academysystem.database.model.Usuario;
import com.david.academysystem.database.repository.IAlunoRepository;
import com.david.academysystem.database.repository.IRoleRepository;
import com.david.academysystem.database.repository.IUsuarioRepository;
import com.david.academysystem.dto.auth.cadastro.AuthAlunoRequestDTO;
import com.david.academysystem.dto.auth.cadastro.AuthRequestDTO;
import com.david.academysystem.dto.auth.cadastro.AuthResponseDTO;
import com.david.academysystem.dto.auth.login.LoginRequestDTO;
import com.david.academysystem.dto.token.TokenResponseDTO;
import com.david.academysystem.enums.RoleTypeEnum;
import com.david.academysystem.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final IUsuarioRepository usuarioRepository;
    private final IRoleRepository roleRepository;
    private final IAlunoRepository alunoRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final TokenProvider tokenProvider;
    @Value("${jwt.expiration}")
    private long expirationTime;

    @Transactional
    public AuthResponseDTO registroAluno(AuthAlunoRequestDTO dto) throws BadRequestException{
        if (usuarioRepository.findByEmail(dto.email()).isPresent()) {
            throw new BadRequestException("Aluno já cadastrado com este e-mail.");
        }

        Role role = roleRepository.findByNome(RoleTypeEnum.ROLE_ALUNO.name())
                        .orElseGet(() -> roleRepository.save(Role.builder()
                                .nome(RoleTypeEnum.ROLE_ALUNO.name()).build()));

        Usuario novoUsuario = usuarioRepository.save(Usuario.builder()
                .email(dto.email())
                .roles(Set.of(role))
                .senha(passwordEncoder.encode(dto.senha()))
                .build());

        alunoRepository.save(Aluno.builder()
                .nome(dto.nome())
                .usuario(novoUsuario)
                .build());

        return new AuthResponseDTO(novoUsuario.getId(), dto.email());
    }

    @Transactional
    public AuthResponseDTO registroFuncionario(AuthRequestDTO dto) throws BadRequestException{
        Usuario usuario = usuarioRepository.findByEmail(dto.email())
                .orElse(null);

        if(usuario != null){
            throw new BadRequestException("Funcionário já cadastrado com este e-mail.");
        }

        Role role = roleRepository.findByNome(RoleTypeEnum.ROLE_FUNCIONARIO.name())
                .orElseGet(() -> roleRepository.save(Role.builder()
                        .nome(RoleTypeEnum.ROLE_FUNCIONARIO.name()).build()));

        Usuario novoUsuario = usuarioRepository.save(Usuario.builder()
                .email(dto.email())
                .roles(Set.of(role))
                .senha(passwordEncoder.encode(dto.senha()))
                .build());

        return new AuthResponseDTO(novoUsuario.getId(), dto.email());
    }

    @Transactional
    public AuthResponseDTO registroAdmin(AuthRequestDTO dto) throws BadRequestException{
        Usuario usuario = usuarioRepository.findByEmail(dto.email())
                .orElse(null);

        if(usuario != null){
            throw new BadRequestException("Admin já cadastrado com este e-mail.");
        }

        Role role = roleRepository.findByNome(RoleTypeEnum.ROLE_ADMIN.name())
                .orElseGet(() -> roleRepository.save(Role.builder()
                        .nome(RoleTypeEnum.ROLE_ADMIN.name()).build()));

        Usuario novoUsuario = usuarioRepository.save(Usuario.builder()
                .email(dto.email())
                .roles(Set.of(role))
                .senha(passwordEncoder.encode(dto.senha()))
                .build());

        return new AuthResponseDTO(novoUsuario.getId(), dto.email());
    }

    public TokenResponseDTO login(LoginRequestDTO dto) throws Exception{
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(dto.email(), dto.senha()));

            String token = tokenProvider.gerarToken(authentication);

            return new TokenResponseDTO(token, expirationTime);

        }catch (BadCredentialsException e){
            throw new BadRequestException("Credenciais inválidas.");

        }catch (Exception e){
            throw e;
        }
    }
}
