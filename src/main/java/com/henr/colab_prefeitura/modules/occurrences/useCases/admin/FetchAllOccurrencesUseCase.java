package com.henr.colab_prefeitura.modules.occurrences.useCases.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.henr.colab_prefeitura.modules.occurrences.dtos.InnerOccurrencesResponseDTO;
import com.henr.colab_prefeitura.modules.occurrences.dtos.OccurrencesResponseDTO;
import com.henr.colab_prefeitura.modules.occurrences.repositories.OccurrenceRepository;

@Service
public class FetchAllOccurrencesUseCase {
    
    @Autowired
    private OccurrenceRepository occurrenceRepository;

    public OccurrencesResponseDTO execute() {
        var occurrences = this.occurrenceRepository.findAll();

          var occurrencesDto = occurrences.stream().map(occurrence -> 
            new InnerOccurrencesResponseDTO(
                occurrence.getId(),
                occurrence.getTitle(),
                occurrence.getDescription(),
                occurrence.getType(),
                occurrence.getStatus(),
                occurrence.getPriority(),
                occurrence.getAddress(),
                occurrence.getLatitude(),
                occurrence.getLongitude(),
                occurrence.getImagePath(),
                occurrence.getUser().getId()
            )
        ).toList();

        return new OccurrencesResponseDTO(occurrencesDto);
    }
}
