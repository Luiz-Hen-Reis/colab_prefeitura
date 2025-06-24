package com.henr.colab_prefeitura.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;



@Configuration
public class RabbitConfig {

    public static final String EXCHANGE = "occurrence.exchange";
    public static final String QUEUE = "occurrence.classify.queue";
    public static final String ROUTING_KEY = "occurrence.classify";

    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(EXCHANGE);
    }

    @Bean
    public Queue classifyQueue() {
        return new Queue(QUEUE, true);
    }

    @Bean
    public Binding binding(Queue classifyQueue, TopicExchange exchange) {
        return BindingBuilder.bind(classifyQueue).to(exchange).with(ROUTING_KEY);
    }
}
