package com.pvlrs.kesar.condition;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

import static java.util.Objects.nonNull;

public class ConsolePasswordReaderCondition implements Condition {

    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        return nonNull(System.console());
    }
}
