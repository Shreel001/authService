package com.bankingApp.authService.service;


import com.bankingApp.authService.entity.Outbox;
import com.bankingApp.authService.kafka.Producer;
import com.bankingApp.authService.repository.OutBoxRepository;
import com.bankingApp.shared_events_library.UserRegisteredEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@Transactional
public class PublishPendingEvents {

    @Autowired
    OutBoxRepository outBoxRepository;

    @Autowired
    private Producer producer;

    @Scheduled(fixedDelay = 5000)
    public void publishPendingEvents(){

        List<Outbox> pendingEvents = outBoxRepository.findByStatus("PENDING");

        for (Outbox outbox : pendingEvents){
            try{
                UserRegisteredEvent event = new ObjectMapper()
                        .readValue(outbox.getPayload(), UserRegisteredEvent.class);

                producer.userRegistered(outbox.getTopic(), event);

                outbox.setStatus("SENT");
                outbox.setProcessedAt(LocalDateTime.now());

            }catch (Exception e){
                outbox.setRetries(outbox.getRetries() + 1);
                outBoxRepository.save(outbox);
            }
        }

    }
}
