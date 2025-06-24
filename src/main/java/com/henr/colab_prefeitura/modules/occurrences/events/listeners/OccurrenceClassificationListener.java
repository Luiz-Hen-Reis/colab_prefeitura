package com.henr.colab_prefeitura.modules.occurrences.events.listeners;

import java.util.Optional;
import java.util.UUID;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.henr.colab_prefeitura.config.RabbitConfig;
import com.henr.colab_prefeitura.modules.occurrences.entities.Occurrence;
import com.henr.colab_prefeitura.modules.occurrences.enums.Priority;
import com.henr.colab_prefeitura.modules.occurrences.jobs.PriorityClassifier;
import com.henr.colab_prefeitura.modules.occurrences.repositories.OccurrenceRepository;

@Component
public class OccurrenceClassificationListener {
    
    @Autowired
    private OccurrenceRepository occurrenceRepository;

    @Autowired
    private PriorityClassifier priorityClassifier;

    @RabbitListener(queues = RabbitConfig.QUEUE)
    public void handle(String occurrenceIdStr) {
        UUID occurrenceId = UUID.fromString(occurrenceIdStr);
        Optional<Occurrence> optional = occurrenceRepository.findById(occurrenceId);

        if (optional.isEmpty()) {
            System.out.println("❌ Ocorrência não encontrada: " + occurrenceId);
            return;
        }

        Occurrence occurrence = optional.get();

        Priority newPriority = this.priorityClassifier.classify(occurrence);

        System.out.printf("🧠 Classificando ocorrência %s como %s%n", occurrenceId, newPriority);

        occurrence.setPriority(newPriority);
        occurrenceRepository.save(occurrence);
    }     
}
