package com.henr.colab_prefeitura.modules.users.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.henr.colab_prefeitura.modules.users.dtos.AuthenticateUserRequestDTO;
import com.henr.colab_prefeitura.modules.users.dtos.AuthenticateUserResponseDTO;
import com.henr.colab_prefeitura.modules.users.dtos.RegisterUserRequestDTO;
import com.henr.colab_prefeitura.modules.users.dtos.RegisterUserResponseDTO;
import com.henr.colab_prefeitura.modules.users.useCases.AuthenticateUserUseCase;
import com.henr.colab_prefeitura.modules.users.useCases.RegisterUserUseCase;

import jakarta.validation.Valid;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

@RestController
@RequestMapping("/auth")
@Tag(name = "Autenticação", description = "Endpoints para registro e autenticação de usuários")
public class AuthController {
   
    @Autowired
    private RegisterUserUseCase registerUserUseCase;

    @Autowired
    private AuthenticateUserUseCase authenticateUserUseCase;

    @Operation(
        summary = "Registrar um novo usuário",
        description = "Cria um novo usuário no sistema com os dados fornecidos no corpo da requisição.",
        responses = {
            @ApiResponse(responseCode = "201", description = "Usuário registrado com sucesso", 
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = RegisterUserResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos na requisição")
        }
    )
    @PostMapping("/register")
    public ResponseEntity<RegisterUserResponseDTO> register(@Valid @RequestBody RegisterUserRequestDTO dto) {
        var result = this.registerUserUseCase.execute(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @Operation(
        summary = "Autenticar usuário",
        description = "Realiza a autenticação do usuário e retorna um token de acesso.",
        responses = {
            @ApiResponse(responseCode = "200", description = "Autenticação realizada com sucesso", 
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = AuthenticateUserResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "Credenciais inválidas")
        }
    )
    @PostMapping("/sessions")
    public ResponseEntity<AuthenticateUserResponseDTO> authenticate(@Valid @RequestBody AuthenticateUserRequestDTO dto) {
        var result = this.authenticateUserUseCase.execute(dto);
        return ResponseEntity.ok().body(result);
    }
}
