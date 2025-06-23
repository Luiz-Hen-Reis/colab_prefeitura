package com.henr.colab_prefeitura.modules.occurrences.useCases;

import com.henr.colab_prefeitura.modules.occurrences.dtos.FetchUserOccurrencesResponseDTO;
import com.henr.colab_prefeitura.modules.occurrences.entities.Occurrence;
import com.henr.colab_prefeitura.modules.occurrences.repositories.OccurrenceRepository;
import com.henr.colab_prefeitura.modules.users.entities.User;
import com.henr.colab_prefeitura.providers.AuthenticatedUserProvider;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public class FetchUserOccurrencesUseCaseTest {

    @Mock
    private OccurrenceRepository occurrenceRepository;

    @Mock
    private AuthenticatedUserProvider authenticatedUserProvider;

    @InjectMocks
    private FetchUserOccurrencesUseCase fetchUserOccurrencesUseCase;

    private User mockUser;

    @BeforeEach
    void setup() {
        mockUser = new User();
        mockUser.setId(UUID.randomUUID());
        mockUser.setName("João da Silva");
        mockUser.setEmail("joao@example.com");
    }

    @Test
    void should_return_user_occurrences() {
        var occurrence1 = new Occurrence();
        occurrence1.setId(UUID.randomUUID());
        occurrence1.setTitle("Buraco na rua");
        occurrence1.setUser(mockUser);

        var occurrence2 = new Occurrence();
        occurrence2.setId(UUID.randomUUID());
        occurrence2.setTitle("Luz queimada");
        occurrence2.setUser(mockUser);

        var expectedOccurrences = List.of(occurrence1, occurrence2);

        when(authenticatedUserProvider.getAuthenticatedUser()).thenReturn(mockUser);
        when(occurrenceRepository.findAllByUserId(mockUser.getId())).thenReturn(expectedOccurrences);

        FetchUserOccurrencesResponseDTO response = fetchUserOccurrencesUseCase.execute();

        assertNotNull(response);
        assertEquals(2, response.occurrences().size());
        assertEquals("Buraco na rua", response.occurrences().get(0).title());

        verify(authenticatedUserProvider, times(1)).getAuthenticatedUser();
        verify(occurrenceRepository, times(1)).findAllByUserId(mockUser.getId());
    }
}
