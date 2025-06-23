package com.henr.colab_prefeitura.modules.occurrences.useCases.admin;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.henr.colab_prefeitura.modules.occurrences.dtos.OccurrencesResponseDTO;
import com.henr.colab_prefeitura.modules.occurrences.entities.Occurrence;
import com.henr.colab_prefeitura.modules.occurrences.repositories.OccurrenceRepository;
import com.henr.colab_prefeitura.modules.users.entities.User;


@ExtendWith(MockitoExtension.class)
public class FetchAllOccurrencesUseCaseTest {

    @Mock
    private OccurrenceRepository occurrenceRepository;

    @InjectMocks
    private FetchAllOccurrencesUseCase fetchAllOccurrencesUseCase;

    private User mockUser;

    @BeforeEach
    void setup() {
        mockUser = new User();
        mockUser.setId(UUID.randomUUID());
        mockUser.setName("João da Silva");
        mockUser.setEmail("joao@example.com");
    }

    @Test
    void should_return_all_occurrences() {
        var occurrence1 = new Occurrence();
        occurrence1.setId(UUID.randomUUID());
        occurrence1.setTitle("Buraco na rua");
        occurrence1.setUser(mockUser);

        var occurrence2 = new Occurrence();
        occurrence2.setId(UUID.randomUUID());
        occurrence2.setTitle("Luz queimada");
        occurrence2.setUser(mockUser);

        var expectedOccurrences = List.of(occurrence1, occurrence2);

        when(occurrenceRepository.findAll()).thenReturn(expectedOccurrences);

         OccurrencesResponseDTO response = fetchAllOccurrencesUseCase.execute();

        assertNotNull(response);
        assertEquals(2, response.occurrences().size());
        assertEquals("Buraco na rua", response.occurrences().get(0).title());
    }
}
