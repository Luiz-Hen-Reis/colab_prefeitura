package com.henr.colab_prefeitura.modules.occurrences.dtos;

import java.math.BigDecimal;

public record CreateOccurrenceRequestDTO(
    String title,
    String description,
    String type,
    String address,
    BigDecimal latitude,
    BigDecimal longitude
) {}
