package com.henr.colab_prefeitura.modules.occurrences.jobs;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.henr.colab_prefeitura.modules.occurrences.entities.Occurrence;
import com.henr.colab_prefeitura.modules.occurrences.enums.Priority;

public class PriorityClassifierTest {

    private final PriorityClassifier classifier = new PriorityClassifier();

    @Test
    void should_return_high_priority() {
        Occurrence occurrence = Occurrence.builder().type("acidente grave").build();
        assertEquals(Priority.HIGH, classifier.classify(occurrence));
    }

    @Test
    void should_return_medium_priority() {
        Occurrence occurrence = Occurrence.builder().type("lixo acumulado").build();
        assertEquals(Priority.MEDIUM, classifier.classify(occurrence));
    }

    @Test
    void should_return_low_priority() {
        Occurrence occurrence = Occurrence.builder().type("barulho").build();
        assertEquals(Priority.LOW, classifier.classify(occurrence));
    }
}
