package com.henr.colab_prefeitura.modules.occurrences.events.publishers;

import java.util.UUID;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import com.henr.colab_prefeitura.config.RabbitConfig;

@Component
public class OccurrenceEventPublisher {
    
    private final RabbitTemplate rabbitTemplate;

    public OccurrenceEventPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publishOccurrenceToClassify(UUID occurrenceId) {
        rabbitTemplate.convertAndSend(
            RabbitConfig.EXCHANGE,
            RabbitConfig.ROUTING_KEY,
            occurrenceId.toString()
        );
    }
}
