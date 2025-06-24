package com.henr.colab_prefeitura.modules.occurrences.useCases.admin;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.henr.colab_prefeitura.exceptions.ResourceNotFoundException;
import com.henr.colab_prefeitura.modules.occurrences.dtos.ChangeOccurrencePriorityResponseDTO;
import com.henr.colab_prefeitura.modules.occurrences.entities.Occurrence;
import com.henr.colab_prefeitura.modules.occurrences.enums.Priority;
import com.henr.colab_prefeitura.modules.occurrences.factories.OccurrenceFactory;
import com.henr.colab_prefeitura.modules.occurrences.repositories.OccurrenceRepository;

@ExtendWith(MockitoExtension.class)
public class ChangeOccurrencePriorityUseCaseTest {

    @Mock
    private OccurrenceRepository occurrenceRepository;

    @InjectMocks
    private ChangeOccurrencePriorityUseCase changeOccurrencePriorityUseCase;

    private Occurrence occurrence;

    @BeforeEach
    void setup() {
        occurrence = OccurrenceFactory.withPriority(Priority.MEDIUM);
    }

    @Test
    void should_change_occurrence_priority_successfully() {
        when(occurrenceRepository.findById(occurrence.getId())).thenReturn(Optional.of(occurrence));
        when(occurrenceRepository.save(any())).thenReturn(occurrence);

        ChangeOccurrencePriorityResponseDTO response = changeOccurrencePriorityUseCase.execute(occurrence.getId(), Priority.HIGH);

        assertEquals("Prioridade da ocorrência atualizada com sucesso!", response.message());
        assertEquals(Priority.HIGH, response.occurrence().priority());
        verify(occurrenceRepository).save(occurrence);
    }

    @Test
    void should_not_update_if_priority_is_the_same() {
        when(occurrenceRepository.findById(occurrence.getId())).thenReturn(Optional.of(occurrence));

        ChangeOccurrencePriorityResponseDTO response = changeOccurrencePriorityUseCase.execute(occurrence.getId(), Priority.MEDIUM);

        assertEquals("Nenhuma alteração realizada: a prioridade atual já é MEDIUM.", response.message());
        assertEquals(Priority.MEDIUM, response.occurrence().priority());
        verify(occurrenceRepository, never()).save(any());
    }

    @Test
    void should_throw_exception_if_occurrence_not_found() {
        UUID invalidId = UUID.randomUUID();
        when(occurrenceRepository.findById(invalidId)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
            ResourceNotFoundException.class,
            () -> changeOccurrencePriorityUseCase.execute(invalidId, Priority.HIGH)
        );

        assertEquals("Ocorrência não encontrada", exception.getMessage());
    }
}
