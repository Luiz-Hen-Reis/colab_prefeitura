package com.henr.colab_prefeitura.exceptions;

public class UserFoundException extends RuntimeException {
    public UserFoundException() {
        super("Já existe um usuário cadastrado com esse e-mail ou CPF");
    }
}
