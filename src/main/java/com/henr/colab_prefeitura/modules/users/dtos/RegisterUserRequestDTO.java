package com.henr.colab_prefeitura.modules.users.dtos;

import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record RegisterUserRequestDTO(
    @NotBlank(message = "Nome é obrigatório")
    String name,

    @NotBlank(message = "CPF é obrigatório")
    @Length(min = 11, max = 11, message = "Um CPF válido deve conter 11 caracteres")
    String CPF,

    @NotBlank(message = "Email é obrigatório")
    @Email(message = "Email inválido")
    String email,

    @NotBlank(message = "Senha é obrigatória")
    @Length(min = 6, message = "A senha precisa ter no mínimo 6 caracteres")
    String password
) {}

