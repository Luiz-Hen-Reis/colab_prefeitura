package com.henr.colab_prefeitura.modules.occurrences.useCases.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.henr.colab_prefeitura.modules.occurrences.dtos.OccurrencesResponseDTO;
import com.henr.colab_prefeitura.modules.occurrences.mappers.OccurrenceMapper;
import com.henr.colab_prefeitura.modules.occurrences.repositories.OccurrenceRepository;

@Service
public class FetchAllOccurrencesUseCase {
    
    @Autowired
    private OccurrenceRepository occurrenceRepository;

    public OccurrencesResponseDTO execute() {
        var occurrences = this.occurrenceRepository.findAllByOrderByCreatedAtDesc();

          var occurrencesDto = occurrences.stream().map(occurrence -> 
          OccurrenceMapper.toInnerDTO(occurrence)).toList();

        return new OccurrencesResponseDTO(occurrencesDto);
    }
}
