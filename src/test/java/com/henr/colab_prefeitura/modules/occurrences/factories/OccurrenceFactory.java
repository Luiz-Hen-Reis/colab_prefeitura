package com.henr.colab_prefeitura.modules.occurrences.factories;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import com.henr.colab_prefeitura.modules.occurrences.entities.Occurrence;
import com.henr.colab_prefeitura.modules.occurrences.enums.Priority;
import com.henr.colab_prefeitura.modules.occurrences.enums.Status;
import com.henr.colab_prefeitura.modules.users.entities.User;

public class OccurrenceFactory {

    public static Occurrence build(Priority priority, Status status) {
        return Occurrence.builder()
            .id(UUID.randomUUID())
            .title("Teste de Ocorrência")
            .description("Descrição de teste")
            .type("buraco")
            .status(status)
            .priority(priority)
            .address("Rua Exemplo, 123")
            .latitude(new BigDecimal("23.5505"))
            .longitude(new BigDecimal("46.6333"))
            .imagePath("/caminho/para/imagem.jpg")
            .user(User.builder().id(UUID.randomUUID()).build())
            .createdAt(LocalDateTime.now())
            .build();
    }

    public static Occurrence withDefaultValues() {
        return build(Priority.LOW, Status.PENDING);
    }

    public static Occurrence withPriority(Priority priority) {
        return build(priority, Status.PENDING);
    }

    public static Occurrence withStatus(Status status) {
        return build(Priority.LOW, status);
    }
}
