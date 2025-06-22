package com.henr.colab_prefeitura.modules.users.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.henr.colab_prefeitura.modules.users.dtos.AuthenticateUserRequestDTO;
import com.henr.colab_prefeitura.modules.users.dtos.AuthenticateUserResponseDTO;
import com.henr.colab_prefeitura.modules.users.dtos.RegisterUserRequestDTO;
import com.henr.colab_prefeitura.modules.users.dtos.RegisterUserResponseDTO;
import com.henr.colab_prefeitura.modules.users.useCases.AuthenticateUserUseCase;
import com.henr.colab_prefeitura.modules.users.useCases.RegisterUserUseCase;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {
   
    @Autowired
    private RegisterUserUseCase registerUserUseCase;

    @Autowired
    private AuthenticateUserUseCase authenticateUserUseCase;

    @PostMapping("/register")
    public ResponseEntity<RegisterUserResponseDTO> register(@Valid @RequestBody RegisterUserRequestDTO dto) {
        var result = this.registerUserUseCase.execute(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @PostMapping("/sessions")
    public ResponseEntity<AuthenticateUserResponseDTO> authenticate(@Valid @RequestBody AuthenticateUserRequestDTO dto) {
        var result = this.authenticateUserUseCase.execute(dto);
        return ResponseEntity.ok().body(result);
    }
}
