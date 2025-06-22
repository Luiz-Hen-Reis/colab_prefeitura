package com.henr.colab_prefeitura.modules.users.useCases;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.henr.colab_prefeitura.exceptions.ResourceNotFoundException;
import com.henr.colab_prefeitura.modules.users.dtos.AuthenticateUserRequestDTO;
import com.henr.colab_prefeitura.modules.users.entities.User;
import com.henr.colab_prefeitura.modules.users.factories.UserFactory;
import com.henr.colab_prefeitura.modules.users.repositories.UserRepository;
import com.henr.colab_prefeitura.providers.JWTUtil;

@ExtendWith(MockitoExtension.class)
public class AuthenticateUserUseCaseTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JWTUtil jwtUtil;

    @InjectMocks
    private AuthenticateUserUseCase authenticateUserUseCase;

    private AuthenticateUserRequestDTO dto;

    @BeforeEach
    void setup() {
        dto = new AuthenticateUserRequestDTO("existingemail@gmail.com", "123456789");
    }

    @Test
    void should_throw_an_exception_if_credentials_is_not_valid() {
        when(this.userRepository.findByEmailOrCPF(dto.login(), dto.login())).thenReturn(
            Optional.empty());

          ResourceNotFoundException thrown = assertThrows(
                ResourceNotFoundException.class,
                () -> authenticateUserUseCase.execute(dto)
            );
         assertEquals("Usuário ou senha incorretos", thrown.getMessage());
    }

    @Test
    void should_authenticate_successfully_and_return_token() {
    var rawPassword = dto.password();

    User fakeUser = UserFactory.createValidUser(passwordEncoder);

    when(userRepository.findByEmailOrCPF(dto.login(), dto.login()))
        .thenReturn(Optional.of(fakeUser));

    when(passwordEncoder.matches(rawPassword, fakeUser.getPassword()))
        .thenReturn(true);

    when(jwtUtil.generateToken(fakeUser.getId().toString(), fakeUser.getRole()))
        .thenReturn("mocked-jwt-token");

    var response = authenticateUserUseCase.execute(dto);

    assertEquals("mocked-jwt-token", response.token());
}
}
