package com.henr.colab_prefeitura.modules.occurrences.jobs;

import org.springframework.stereotype.Component;

import com.henr.colab_prefeitura.modules.occurrences.entities.Occurrence;
import com.henr.colab_prefeitura.modules.occurrences.enums.Priority;

@Component
public class PriorityClassifier {
    
    public Priority classify(Occurrence occurrence) {
        String type = occurrence.getType().toLowerCase();

        if (type.contains("acidente") || type.contains("falta de energia")) {
            return Priority.HIGH;
        }

        if (type.contains("buraco") || type.contains("lixo")) {
            return Priority.MEDIUM;
        }

        return Priority.LOW;
    }
}
