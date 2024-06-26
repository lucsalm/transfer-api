package com.lucasalmd.transfer.providers.integrations;


import com.lucasalmd.transfer.domain.exceptions.BusinessException;
import com.lucasalmd.transfer.domain.exceptions.ErrorMessage;
import com.lucasalmd.transfer.domain.integrations.AuthorizationResponse;
import com.lucasalmd.transfer.domain.integrations.NotificationResponse;
import com.lucasalmd.transfer.domain.models.Transfer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class MockyClient {

    private final WebClient mockyWebClient;

    @Value("${integrations.mocky.endpoint.authorization}")
    private String authorizationPath;

    @Value("${integrations.mocky.endpoint.notification}")
    private String notificationPath;


    public Mono<AuthorizationResponse> authorize(Transfer infoTransaction) {
        return mockyWebClient.post()
                .uri(authorizationPath)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(infoTransaction))
                .retrieve()
                .bodyToMono(AuthorizationResponse.class)
                .onErrorResume(error -> Mono.error(new BusinessException(ErrorMessage.AUTHORIZATION_REQUEST_FAILED)));
    }

    public Mono<NotificationResponse> notify(Transfer infoTransaction) {
        return mockyWebClient.post()
                .uri(notificationPath)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(infoTransaction))
                .retrieve()
                .bodyToMono(NotificationResponse.class)
                .onErrorResume(error -> Mono.error(new BusinessException(ErrorMessage.NOTIFICATION_REQUEST_FAILED)));

    }
}
