package com.henr.colab_prefeitura.modules.occurrences.dtos;

import java.util.List;

public record OccurrencesResponseDTO(
    List<InnerOccurrencesResponseDTO> occurrences
) {}
