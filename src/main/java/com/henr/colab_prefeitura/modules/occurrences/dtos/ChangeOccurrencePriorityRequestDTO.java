package com.henr.colab_prefeitura.modules.occurrences.dtos;

import com.henr.colab_prefeitura.modules.occurrences.enums.Priority;

import jakarta.validation.constraints.NotNull;

public record ChangeOccurrencePriorityRequestDTO(
    @NotNull(message = "A prioridade deve ser informada.")
    Priority priority
    ) {}
