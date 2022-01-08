package com.pvlrs.kesar.condition;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

import static java.util.Objects.isNull;

public class StandardInputPasswordReaderCondition implements Condition {

    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        return isNull(System.console());
    }
}
