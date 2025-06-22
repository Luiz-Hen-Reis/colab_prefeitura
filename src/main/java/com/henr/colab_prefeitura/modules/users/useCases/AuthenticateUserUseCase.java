package com.henr.colab_prefeitura.modules.users.useCases;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.henr.colab_prefeitura.exceptions.ResourceNotFoundException;
import com.henr.colab_prefeitura.modules.users.dtos.AuthenticateUserRequestDTO;
import com.henr.colab_prefeitura.modules.users.dtos.AuthenticateUserResponseDTO;
import com.henr.colab_prefeitura.modules.users.entities.User;
import com.henr.colab_prefeitura.modules.users.repositories.UserRepository;
import com.henr.colab_prefeitura.providers.JWTUtil;

@Service
public class AuthenticateUserUseCase {
    
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JWTUtil jwtUtil;

    public AuthenticateUserResponseDTO execute(AuthenticateUserRequestDTO dto) {
        String login =  dto.login();

        Optional<User> userOpt = this.userRepository.findByEmailOrCPF(login, login);

        if (userOpt.isEmpty()) {
            throw new ResourceNotFoundException("Usuário ou senha incorretos");
        }

        User user = userOpt.get();

        boolean passwordMatches = this.passwordEncoder.matches(dto.password(), user.getPassword());

        if (!passwordMatches) {
            throw new ResourceNotFoundException("Usuário ou senha incorretos");
        }
        
        var token = jwtUtil.generateToken(user.getId().toString(), user.getRole());

        return new AuthenticateUserResponseDTO(token);
    }
}
