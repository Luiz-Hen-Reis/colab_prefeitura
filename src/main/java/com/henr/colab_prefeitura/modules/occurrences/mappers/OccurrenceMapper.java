package com.henr.colab_prefeitura.modules.occurrences.mappers;

import com.henr.colab_prefeitura.modules.occurrences.dtos.InnerOccurrencesResponseDTO;
import com.henr.colab_prefeitura.modules.occurrences.entities.Occurrence;

public class OccurrenceMapper {

    public static InnerOccurrencesResponseDTO toInnerDTO(Occurrence occurrence) {
        return new InnerOccurrencesResponseDTO(
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
        );
    }
}