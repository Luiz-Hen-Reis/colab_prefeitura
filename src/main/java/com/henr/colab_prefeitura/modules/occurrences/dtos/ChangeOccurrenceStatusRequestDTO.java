package com.henr.colab_prefeitura.modules.occurrences.dtos;

import com.henr.colab_prefeitura.modules.occurrences.enums.Status;

import jakarta.validation.constraints.NotNull;

public record ChangeOccurrenceStatusRequestDTO(
    @NotNull(message = "O status deve ser informado.")
    Status status
) {}