package com.lucasalmd.transfer.providers.producer;

import com.lucasalmd.transfer.domain.exceptions.BusinessException;
import com.lucasalmd.transfer.domain.exceptions.ErrorMessage;
import com.lucasalmd.transfer.domain.models.Transfer;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class NotificationProducer {

    private final ReactiveKafkaProducerTemplate<String, Transfer> producer;

    @Value("${kafka.server.topic}")
    private String topic;

    public Mono<Void> sendMessage(Transfer transferDTO) {
        return producer.send(topic, transferDTO)
                .timeout(Duration.ofMillis(3000))
                .onErrorResume(error -> Mono.error(new BusinessException(ErrorMessage.PRODUCE_NOTIFICATION_FAILED)))
                .then();
    }
}
