package com.henr.colab_prefeitura.modules.users.enums;

import lombok.Getter;

@Getter
public enum Role {
    ADMIN("ROLE_ADMIN"),
    USER("ROLE_USER");

    private final String roleName;

    Role(String role) {
        this.roleName = role;
    }
}
