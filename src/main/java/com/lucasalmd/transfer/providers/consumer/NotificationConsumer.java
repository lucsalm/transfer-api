package com.lucasalmd.transfer.providers.consumer;

import com.lucasalmd.transfer.domain.models.Transfer;
import com.lucasalmd.transfer.providers.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.core.reactive.ReactiveKafkaConsumerTemplate;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class NotificationConsumer {

    @Autowired
    private ReactiveKafkaConsumerTemplate<String, Transfer> reactiveKafkaConsumerTemplate;

    @Autowired
    private NotificationService notificationService;

    @EventListener(ApplicationStartedEvent.class)
    public Flux<Void> listen() {
        return reactiveKafkaConsumerTemplate.receive()
                .doOnNext(consumerRecord -> log.info("Cosuming record: key={}, value={} from topic={}, offset={}",
                        consumerRecord.key(), consumerRecord.value(), consumerRecord.topic(), consumerRecord.offset()))
                .flatMap(consumerRecord ->
                        notificationService.sendNotification(consumerRecord.value())
                                .doOnSuccess(success -> {
                                    log.info("Success to consume record: key={}, value={} from topic={}, offset={}",
                                            consumerRecord.key(), consumerRecord.value(), consumerRecord.topic(), consumerRecord.offset());
                                    consumerRecord.receiverOffset().acknowledge();
                                }).onErrorResume(error -> {
                                    log.info("Failed to consume record: key={}, value={} from topic={}, offset={}",
                                            consumerRecord.key(), consumerRecord.value(), consumerRecord.topic(), consumerRecord.offset());
                                    return Mono.empty();
                                })
                );
    }
}
