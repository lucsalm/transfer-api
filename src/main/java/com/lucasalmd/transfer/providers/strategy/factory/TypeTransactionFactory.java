package com.lucasalmd.transfer.providers.strategy.factory;

import com.lucasalmd.transfer.domain.entities.Account;
import com.lucasalmd.transfer.domain.enums.TypeEnum;
import com.lucasalmd.transfer.domain.exceptions.BusinessException;
import com.lucasalmd.transfer.domain.exceptions.ErrorMessage;
import com.lucasalmd.transfer.providers.strategy.TransactionStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

@Component
public class TypeTransactionFactory {

    private Map<TypeEnum, TransactionStrategy> strategyMap = new EnumMap<>(TypeEnum.class);

    public TypeTransactionFactory(@Autowired Set<TransactionStrategy> strategies) {
        strategies.forEach(strategy -> strategyMap.put(strategy.getType(), strategy));
    }

    public TransactionStrategy getStrategy(Account account) {
        TransactionStrategy strategy = strategyMap.get(account.getType());
        if (Objects.isNull(strategy)) {
            throw new BusinessException(ErrorMessage.PAYER_TYPE_IS_INVALID);
        }
        return strategy;
    }


}
