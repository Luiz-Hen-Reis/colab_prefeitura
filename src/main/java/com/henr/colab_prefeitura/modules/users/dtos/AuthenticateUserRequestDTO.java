package com.henr.colab_prefeitura.modules.users.dtos;

import org.hibernate.validator.constraints.Length;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record AuthenticateUserRequestDTO(
    @NotBlank(message = "O campo usuário é obrigatório")
     @Schema(description = "Email ou CPF do usuário", example = "joao@example.com")
    String login,

    @NotBlank(message = "Senha é obrigatória")
    @Length(min = 6, message = "A senha precisa ter no mínimo 6 caracteres")
    @Schema(description = "Senha do usuário", example = "senha123")
    String password
) {}
