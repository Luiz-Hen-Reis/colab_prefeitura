package com.henr.colab_prefeitura.modules.occurrences.useCases.admin;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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

    @Test
    void should_change_occurrence_priority_successfully() {
        Occurrence occurrence = OccurrenceFactory.withPriority(Priority.LOW);

        when(occurrenceRepository.findById(occurrence.getId())).thenReturn(Optional.of(occurrence));
        when(occurrenceRepository.save(any())).thenReturn(occurrence);

        ChangeOccurrencePriorityResponseDTO response = changeOccurrencePriorityUseCase.execute(occurrence.getId(), Priority.HIGH);

        assertEquals("Prioridade da ocorrência atualizada com sucesso!", response.message());
        assertEquals(Priority.HIGH, response.occurrence().priority());
        verify(occurrenceRepository).save(occurrence);
    }
}
