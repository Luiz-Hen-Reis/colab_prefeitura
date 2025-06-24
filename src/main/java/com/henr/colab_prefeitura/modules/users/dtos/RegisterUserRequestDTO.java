package com.henr.colab_prefeitura.modules.users.dtos;

import org.hibernate.validator.constraints.Length;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record RegisterUserRequestDTO(
    @NotBlank(message = "Nome é obrigatório")
    @Schema(description = "Nome completo do usuário", example = "João da Silva")
    String name,

    @NotBlank(message = "CPF é obrigatório")
    @Length(min = 11, max = 11, message = "Um CPF válido deve conter 11 caracteres")
    @Schema(description = "CPF do usuário, 11 dígitos", example = "12345678901")
    String CPF,

    @NotBlank(message = "Email é obrigatório")
    @Email(message = "Email inválido")
    @Schema(description = "Email do usuário", example = "joao@example.com")
    String email,

    @NotBlank(message = "Senha é obrigatória")
    @Length(min = 6, message = "A senha precisa ter no mínimo 6 caracteres")
    @Schema(description = "Senha do usuário", example = "senha123")
    String password
) {}

