package com.example.kafka_producer.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ReceiverData {
    private String receiverEmail;
    private String receiverName;

    @Override
    public String toString() {
        return String.format("%s|%s$", receiverEmail, receiverName);
    }
}
