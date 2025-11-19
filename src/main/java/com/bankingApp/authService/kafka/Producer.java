package com.bankingApp.authService.kafka;

import com.bankingApp.shared_events_library.UserRegisteredEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class Producer {

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    public void userRegistered(String Topic, UserRegisteredEvent userRegisteredEvent){
        kafkaTemplate.send(Topic, userRegisteredEvent);
    }

}
