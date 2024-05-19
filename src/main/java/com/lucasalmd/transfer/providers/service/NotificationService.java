package com.lucasalmd.transfer.providers.service;

import com.lucasalmd.transfer.domain.integrations.NotificationResponse;
import com.lucasalmd.transfer.domain.models.Transfer;
import com.lucasalmd.transfer.domain.exceptions.BusinessException;
import com.lucasalmd.transfer.domain.exceptions.Message;
import com.lucasalmd.transfer.providers.integrations.MockyClient;
import com.lucasalmd.transfer.providers.producer.NotificationProducer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class NotificationService {

    @Autowired
    private MockyClient mockyClient;

    @Autowired
    private NotificationProducer producer;


    public Mono<Void> sendNotification(Transfer transferDTO) {
        return mockyClient.notify(transferDTO)
                .map(NotificationResponse::message)
                .flatMap(message -> {
                    if (!message) {
                        return Mono.error(new BusinessException(Message.NOTIFICATION_SEND_FAILED));
                    }
                    return Mono.empty();
                });
    }

    public Mono<Void> produceNotification(Transfer transferDTO) {
        return producer.sendMessage(transferDTO);
    }


}
