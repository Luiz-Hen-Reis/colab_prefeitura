package com.henr.colab_prefeitura.modules.occurrences.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.henr.colab_prefeitura.modules.occurrences.entities.Occurrence;

public interface OccurrenceRepository extends JpaRepository<Occurrence, UUID> {   
}
