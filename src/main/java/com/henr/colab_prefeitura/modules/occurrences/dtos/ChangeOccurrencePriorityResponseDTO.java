package com.henr.colab_prefeitura.modules.occurrences.dtos;

public record ChangeOccurrencePriorityResponseDTO(
    String message,
    InnerOccurrencesResponseDTO occurrence
) {}