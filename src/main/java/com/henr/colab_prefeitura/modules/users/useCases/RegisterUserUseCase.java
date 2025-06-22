package com.henr.colab_prefeitura.modules.users.useCases;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.henr.colab_prefeitura.exceptions.UserFoundException;
import com.henr.colab_prefeitura.modules.users.dtos.RegisterUserRequestDTO;
import com.henr.colab_prefeitura.modules.users.dtos.RegisterUserResponseDTO;
import com.henr.colab_prefeitura.modules.users.entities.User;
import com.henr.colab_prefeitura.modules.users.enums.Role;
import com.henr.colab_prefeitura.modules.users.repositories.UserRepository;
import com.henr.colab_prefeitura.providers.JWTUtil;

@Service
public class RegisterUserUseCase {
    
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JWTUtil jwtUtil;

    public RegisterUserResponseDTO execute(RegisterUserRequestDTO dto) {
        Optional<User> userExists = this.userRepository.findByEmailOrCPF(dto.email(), dto.CPF());

        if (userExists.isPresent()) {
            throw new UserFoundException();
        }

        var hashedPassword = this.passwordEncoder.encode(dto.password());

        User newUser = User
                            .builder()
                            .name(dto.name())
                            .email(dto.email())
                            .CPF(dto.CPF())
                            .password(hashedPassword)
                            .role(Role.USER)
                            .build();

        User savedUser = this.userRepository.save(newUser);

        var token = jwtUtil.generateToken(savedUser.getId().toString(), savedUser.getRole());

        return new RegisterUserResponseDTO(token);
    }
}
