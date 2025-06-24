package com.henr.colab_prefeitura.modules.occurrences.useCases.admin;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.henr.colab_prefeitura.modules.occurrences.dtos.ChangeOccurrenceStatusResponseDTO;
import com.henr.colab_prefeitura.modules.occurrences.entities.Occurrence;
import com.henr.colab_prefeitura.modules.occurrences.enums.Status;
import com.henr.colab_prefeitura.modules.occurrences.factories.OccurrenceFactory;
import com.henr.colab_prefeitura.modules.occurrences.repositories.OccurrenceRepository;

@ExtendWith(MockitoExtension.class)
public class ChangeOccurrenceStatusUseCaseTest {

    @Mock
    private OccurrenceRepository occurrenceRepository;

    @InjectMocks
    private ChangeOccurrenceStatusUseCase changeOccurrenceStatusUseCase;

    private Occurrence occurrence;

    @BeforeEach
    void setup() {
        occurrence = OccurrenceFactory.withStatus(Status.PENDING);
    }

    @Test
    void should_change_occurrence_status_successfully() {

        when(occurrenceRepository.findById(occurrence.getId())).thenReturn(Optional.of(occurrence));
        when(occurrenceRepository.save(any())).thenReturn(occurrence);

        ChangeOccurrenceStatusResponseDTO response = changeOccurrenceStatusUseCase.execute(occurrence.getId(), Status.RESOLVED);

        assertEquals("Status da ocorrência atualizada com sucesso!", response.message());
        assertEquals(Status.RESOLVED, response.occurrence().status());
        verify(occurrenceRepository).save(occurrence);
    }

    @Test
    void should_not_update_if_status_is_the_same() {
        when(occurrenceRepository.findById(occurrence.getId())).thenReturn(Optional.of(occurrence));

        ChangeOccurrenceStatusResponseDTO response = changeOccurrenceStatusUseCase.execute(occurrence.getId(), Status.PENDING);

        assertEquals("Nenhuma alteração realizada: a prioridade atual já é PENDING.", response.message());
        assertEquals(Status.PENDING, response.occurrence().status());
        verify(occurrenceRepository, never()).save(any());
    }

    @Test
    void should_throw_exception_if_occurrence_not_found() {
        UUID invalidId = UUID.randomUUID();
        when(occurrenceRepository.findById(invalidId)).thenReturn(Optional.empty());

        var exception = org.junit.jupiter.api.Assertions.assertThrows(
            com.henr.colab_prefeitura.exceptions.ResourceNotFoundException.class,
            () -> changeOccurrenceStatusUseCase.execute(invalidId, Status.RESOLVED)
        );

        assertEquals("Ocorrência não encontrada", exception.getMessage());
    }
}
