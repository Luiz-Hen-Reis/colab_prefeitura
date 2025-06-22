package com.henr.colab_prefeitura.providers;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.henr.colab_prefeitura.contracts.AuthenticatedUserProviderContract;
import com.henr.colab_prefeitura.exceptions.ResourceNotFoundException;
import com.henr.colab_prefeitura.modules.users.entities.User;
import com.henr.colab_prefeitura.modules.users.repositories.UserRepository;

import jakarta.servlet.http.HttpServletRequest;

@Component
public class AuthenticatedUserProvider implements AuthenticatedUserProviderContract {

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private UserRepository userRepository;

    @Override
    public User getAuthenticatedUser() {
        String userIdStr = (String) request.getAttribute("user_id");
        if (userIdStr == null) {
            throw new ResourceNotFoundException("Usuário não autenticado");
        }

        return userRepository.findById(UUID.fromString(userIdStr))
            .orElseThrow(() -> new ResourceNotFoundException("Usuário não autenticado"));
    }
    
}
