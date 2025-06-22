package com.henr.colab_prefeitura.modules.users.useCases;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.henr.colab_prefeitura.exceptions.UserFoundException;
import com.henr.colab_prefeitura.modules.users.dtos.RegisterUserRequestDTO;
import com.henr.colab_prefeitura.modules.users.dtos.RegisterUserResponseDTO;
import com.henr.colab_prefeitura.modules.users.entities.User;
import com.henr.colab_prefeitura.modules.users.enums.Role;
import com.henr.colab_prefeitura.modules.users.repositories.UserRepository;
import com.henr.colab_prefeitura.providers.JWTUtil;

@ExtendWith(MockitoExtension.class)
public class RegisterUserUseCaseTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JWTUtil jwtUtil;

    @InjectMocks
    private RegisterUserUseCase registerUserUseCase;

    @Captor
    private ArgumentCaptor<User> userCaptor;

    private RegisterUserRequestDTO dto;

    @BeforeEach
    void setup() {
        dto = new RegisterUserRequestDTO("Fulano de Tal", "existingemail@gmail.com", "12345678901", "123456789");
    }

    @Test
    void should_throw_exception_when_user_already_exists() {
         when(userRepository.findByEmailOrCPF(dto.email(), dto.CPF())).thenReturn(
            Optional.of(new User())
         );

         assertThrows(UserFoundException.class, () -> registerUserUseCase.execute(dto));
         verify(userRepository, never()).save(any());
    }

    @Test
    void should_register_a_user_and_return_token() {
        when(userRepository.findByEmailOrCPF(dto.email(), dto.CPF())).thenReturn(Optional.empty());
        
        var encodedPassword = passwordEncoder.encode(dto.password());
        User savedUser = User.builder()
            .id(UUID.randomUUID())
            .name(dto.name())
            .email(dto.email())
            .CPF(dto.CPF())
            .password(encodedPassword)
            .role(Role.USER)
            .build();

        when(userRepository.save(any(User.class))).thenReturn(savedUser);
        when(jwtUtil.generateToken(savedUser.getId().toString(), savedUser.getRole())).thenReturn("mocked-jwt-token");

         RegisterUserResponseDTO response = registerUserUseCase.execute(dto);

        assertNotNull(response);
        assertEquals(encodedPassword, savedUser.getPassword());
        assertEquals("mocked-jwt-token", response.token());
        verify(userRepository).save(userCaptor.capture());
        
        User capturedUser = userCaptor.getValue();

        assertEquals(dto.name(), capturedUser.getName());
        assertEquals(encodedPassword, capturedUser.getPassword());
    }
}
