package com.henr.colab_prefeitura.modules.users.factories;

import java.util.UUID;

import com.henr.colab_prefeitura.modules.users.entities.User;
import com.henr.colab_prefeitura.modules.users.enums.Role;
import org.springframework.security.crypto.password.PasswordEncoder;

public class UserFactory {

    public static User createValidUser(PasswordEncoder encoder) {
        return User.builder()
            .id(UUID.randomUUID())
            .name("João da Silva")
            .email("joao@gmail.com")
            .CPF("12345678901")
            .password(encoder.encode("123456"))
            .role(Role.USER)
            .build();
    }

    public static User createAdminUser(PasswordEncoder encoder) {
        return User.builder()
            .id(UUID.randomUUID())
            .name("Admin")
            .email("admin@prefeitura.com")
            .CPF("99999999999")
            .password(encoder.encode("admin123"))
            .role(Role.ADMIN)
            .build();
    }
}
