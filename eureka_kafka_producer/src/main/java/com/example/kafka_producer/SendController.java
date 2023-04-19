package com.example.kafka_producer;

import com.example.kafka_producer.dto.EmailRequest;
import com.example.kafka_producer.dto.ReceiverData;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/send")
@RestController
public class SendController {

    private final MessageProducer producer;
    
    @Operation(summary = "이메일 발송 메서드", description = "이메일을 발송합니다.")
    @PostMapping
    public String sendMessage(@RequestBody EmailRequest emailRequest) {
        String messageInfo = emailRequest.toString();
        log.info(">> 메시지 요청: {}", emailRequest.toString());
        StringBuilder sb = new StringBuilder();
        sb.append(messageInfo);
        sb.append("$$");
        for (ReceiverData receiver : emailRequest.getReceivers()) {
            sb.append(receiver.toString());
        }
        producer.sendMessage("myTopic", sb.toString());
        return "OK";
    }
}
