package com.henr.colab_prefeitura.modules.occurrences.dtos;

import java.math.BigDecimal;
import java.util.UUID;

import com.henr.colab_prefeitura.modules.occurrences.enums.Priority;
import com.henr.colab_prefeitura.modules.occurrences.enums.Status;

public record InnerOccurrencesResponseDTO(
    UUID id,
    String title,
    String description,
    String type,
    Status status,
    Priority priority,
    String address,
    BigDecimal latitude,
    BigDecimal longitude,
    String imagePath,
    UUID userId
) {}
