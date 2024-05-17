package com.lucasalmd.transfer.providers.service;

import com.lucasalmd.transfer.domain.integrations.AuthorizationResponse;
import com.lucasalmd.transfer.domain.models.Transfer;
import com.lucasalmd.transfer.domain.exceptions.BusinessException;
import com.lucasalmd.transfer.domain.exceptions.Message;
import com.lucasalmd.transfer.providers.integrations.MockyClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class AuthorizationService {

    private final static String AUTHORIZED = "Autorizado";

    @Autowired
    private MockyClient mockyClient;


    public Mono<AuthorizationResponse> authorize(Transfer transferDTO) {
        return mockyClient.authorize(transferDTO)
                .flatMap(authorization -> {
                    if(!AUTHORIZED.equals(authorization.message())){
                        return Mono.error(new BusinessException(Message.UNAUTHORIZED_TRANSFER));
                    }
                    return Mono.empty();
                });
    }
}
