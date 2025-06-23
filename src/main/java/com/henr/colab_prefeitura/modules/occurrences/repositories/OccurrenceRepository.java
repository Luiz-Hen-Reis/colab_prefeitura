package com.henr.colab_prefeitura.modules.occurrences.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.henr.colab_prefeitura.modules.occurrences.entities.Occurrence;

public interface OccurrenceRepository extends JpaRepository<Occurrence, UUID> {
    List<Occurrence> findAllByUserId(UUID userId);   
}
