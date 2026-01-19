package com.bankingApp.authService.kafka;

import com.bankingApp.shared_events_library.UserRegisteredEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;

@Service
@Slf4j
public class Producer {

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    public void userRegistered(String Topic, UserRegisteredEvent userRegisteredEvent) throws Exception {
        try {
            kafkaTemplate.send(Topic, userRegisteredEvent).get();
        } catch (Exception e) {
            log.error("Kafka error: {}", e.getMessage(), e);
            throw new RuntimeException("Unable to send User Registration Kafka event");
        }
    }

}
