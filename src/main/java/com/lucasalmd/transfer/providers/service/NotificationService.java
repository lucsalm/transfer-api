package com.lucasalmd.transfer.providers.service;

import com.lucasalmd.transfer.domain.exceptions.BusinessException;
import com.lucasalmd.transfer.domain.exceptions.ErrorMessage;
import com.lucasalmd.transfer.domain.integrations.NotificationResponse;
import com.lucasalmd.transfer.domain.models.Transfer;
import com.lucasalmd.transfer.providers.integrations.MockyClient;
import com.lucasalmd.transfer.providers.producer.NotificationProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationService {

    private final MockyClient mockyClient;

    private final NotificationProducer producer;


    public Mono<Void> sendNotification(Transfer transferDTO) {
        return mockyClient.notify(transferDTO)
                .map(NotificationResponse::message)
                .flatMap(message -> {
                    if (Boolean.FALSE.equals(message)) {
                        return Mono.error(new BusinessException(ErrorMessage.NOTIFICATION_SEND_FAILED));
                    }
                    return Mono.empty();
                });
    }

    public Mono<Void> produceNotification(Transfer transferDTO) {
        return producer.sendMessage(transferDTO);
    }


}
