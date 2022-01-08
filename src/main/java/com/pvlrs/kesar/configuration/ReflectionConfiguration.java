package com.pvlrs.kesar.configuration;

import com.pvlrs.kesar.condition.ConsolePasswordReaderCondition;
import com.pvlrs.kesar.condition.StandardInputPasswordReaderCondition;
import org.springframework.context.annotation.Configuration;
import org.springframework.nativex.hint.TypeHint;

/* Ensures these types are accessible via reflection at runtime (in the native image)
   More info at: https://www.graalvm.org/reference-manual/native-image/Reflection and
   https://docs.spring.io/spring-native/docs/current/reference/htmlsingle/#native-hints */
@TypeHint(types = {
        StandardInputPasswordReaderCondition.class,
        ConsolePasswordReaderCondition.class
})
@Configuration
public class ReflectionConfiguration {
}
