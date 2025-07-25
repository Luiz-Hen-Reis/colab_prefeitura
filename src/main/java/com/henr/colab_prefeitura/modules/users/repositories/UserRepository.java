package com.henr.colab_prefeitura.modules.users.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.henr.colab_prefeitura.modules.users.entities.User;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByEmailOrCPF(String email, String CPF);
}
