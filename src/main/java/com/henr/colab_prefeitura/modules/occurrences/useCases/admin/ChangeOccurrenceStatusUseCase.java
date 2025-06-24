package com.henr.colab_prefeitura.modules.occurrences.useCases.admin;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.henr.colab_prefeitura.exceptions.ResourceNotFoundException;
import com.henr.colab_prefeitura.modules.occurrences.dtos.ChangeOccurrenceStatusResponseDTO;
import com.henr.colab_prefeitura.modules.occurrences.enums.Status;
import com.henr.colab_prefeitura.modules.occurrences.mappers.OccurrenceMapper;
import com.henr.colab_prefeitura.modules.occurrences.repositories.OccurrenceRepository;

@Service
public class ChangeOccurrenceStatusUseCase {
    
    @Autowired
    private OccurrenceRepository occurrenceRepository;
    
    public ChangeOccurrenceStatusResponseDTO execute(UUID occurrenceId, Status status) {
        var occurrence = this.occurrenceRepository.findById(occurrenceId)
            .orElseThrow(() -> new ResourceNotFoundException("Ocorrência não encontrada"));
        
        
        if (occurrence.getStatus() == status) {
            return new ChangeOccurrenceStatusResponseDTO("Nenhuma alteração realizada: a prioridade atual já é " + status.name() + ".",
            OccurrenceMapper.toInnerDTO(occurrence));
        }

        occurrence.setStatus(status);
        this.occurrenceRepository.save(occurrence);
        return new ChangeOccurrenceStatusResponseDTO("Status da ocorrência atualizada com sucesso!", 
        OccurrenceMapper.toInnerDTO(occurrence));
    }
}
