package com.henr.colab_prefeitura.modules.occurrences.dtos;

public record ChangeOccurrenceStatusResponseDTO(
    String message,
    InnerOccurrencesResponseDTO occurrence
) {}
