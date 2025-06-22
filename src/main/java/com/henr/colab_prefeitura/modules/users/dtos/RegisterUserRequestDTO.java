package com.henr.colab_prefeitura.modules.users.dtos;

public record RegisterUserRequestDTO(
    String name,
    String email,
    String CPF,
    String password
) {}
