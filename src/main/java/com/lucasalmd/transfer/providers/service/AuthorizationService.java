package com.lucasalmd.transfer.providers.service;

import com.lucasalmd.transfer.domain.exceptions.BusinessException;
import com.lucasalmd.transfer.domain.exceptions.ErrorMessage;
import com.lucasalmd.transfer.domain.integrations.AuthorizationResponse;
import com.lucasalmd.transfer.domain.models.Transfer;
import com.lucasalmd.transfer.providers.integrations.MockyClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class AuthorizationService {

    private static final String AUTHORIZED = "Autorizado";


    private final MockyClient mockyClient;


    public Mono<AuthorizationResponse> authorize(Transfer transferDTO) {
        return mockyClient.authorize(transferDTO)
                .flatMap(authorization -> {
                    if(!AUTHORIZED.equals(authorization.message())){
                        return Mono.error(new BusinessException(ErrorMessage.UNAUTHORIZED_TRANSFER));
                    }
                    return Mono.empty();
                });
    }
}
