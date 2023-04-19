package com.example.kafka_producer.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter @Setter
public class CustomMessageData {
    private String messageKey;
    private String senderEmail;
    private String title;
    private String message;
    private String receiverEmail;
    private String receiverName;
    private LocalDateTime createdDate;

    @Override
    public String toString() {
        return String.format(
                "%s|%s|%s|%s|%s|%s|%s",
                messageKey,
                senderEmail,
                title,
                message,
                receiverEmail,
                receiverName,
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
        );
    }
}
