package com.henr.colab_prefeitura.modules.occurrences.dtos;

import java.math.BigDecimal;

import com.henr.colab_prefeitura.modules.occurrences.enums.Priority;
import com.henr.colab_prefeitura.modules.occurrences.enums.Status;

public record CreateOccurrenceResponseDTO(
    String title,
    String description,
    String type,
    String address,
    BigDecimal latitude,
    BigDecimal longitude,
    String image_path,
    Priority priority,
    Status status,
    String user_id
) {}
