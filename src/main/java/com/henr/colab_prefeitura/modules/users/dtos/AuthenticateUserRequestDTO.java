package com.henr.colab_prefeitura.modules.users.dtos;

import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.NotBlank;

public record AuthenticateUserRequestDTO(
    @NotBlank(message = "O campo usuário é obrigatório")
    String login,

    @NotBlank(message = "Senha é obrigatória")
    @Length(min = 6, message = "A senha precisa ter no mínimo 6 caracteres")
    String password
) {}
