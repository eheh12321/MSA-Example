package com.example.kafka_producer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Slf4j
@RequiredArgsConstructor
@Component
public class MessageProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public void sendMessage(String topic, String msg) {
        CompletableFuture<SendResult<String, String>> future = kafkaTemplate.send(topic, msg);
        future.whenComplete((result, exception) -> {
            if (exception == null) {
                log.info(">> [{}] Kafka({})에 메시지를 Produce 했습니다", topic, msg);
            } else {
                log.error(">> Kafka Produce Exception: {}", exception.getMessage());
            }
        });
    }
}
