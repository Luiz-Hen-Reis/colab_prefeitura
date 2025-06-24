package com.henr.colab_prefeitura.modules.occurrences.useCases.admin;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.henr.colab_prefeitura.exceptions.ResourceNotFoundException;
import com.henr.colab_prefeitura.modules.occurrences.dtos.ChangeOccurrencePriorityResponseDTO;
import com.henr.colab_prefeitura.modules.occurrences.enums.Priority;
import com.henr.colab_prefeitura.modules.occurrences.mappers.OccurrenceMapper;
import com.henr.colab_prefeitura.modules.occurrences.repositories.OccurrenceRepository;

@Service
public class ChangeOccurrencePriorityUseCase {

    @Autowired
    private OccurrenceRepository occurrenceRepository;
    
    public ChangeOccurrencePriorityResponseDTO execute(UUID occurrenceId, Priority priority) {
        var occurrence = this.occurrenceRepository.findById(occurrenceId)
            .orElseThrow(() -> new ResourceNotFoundException("Ocorrência não encontrada"));
        
        
        if (occurrence.getPriority() == priority) {
            return new ChangeOccurrencePriorityResponseDTO("Nenhuma alteração realizada: a prioridade atual já é " + priority.name() + ".",
            OccurrenceMapper.toInnerDTO(occurrence));
        }

        occurrence.setPriority(priority);
        this.occurrenceRepository.save(occurrence);
        return new ChangeOccurrencePriorityResponseDTO("Prioridade da ocorrência atualizada com sucesso!", 
        OccurrenceMapper.toInnerDTO(occurrence));
    }
}
