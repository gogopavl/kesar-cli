package com.pvlrs.kesar.utility;

import com.pvlrs.kesar.condition.ConsolePasswordReaderCondition;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Component;

import static com.pvlrs.kesar.constant.ConsoleMessages.PASSWORD_PROMPT;

@Component
@Conditional(ConsolePasswordReaderCondition.class)
public class ConsolePasswordReader implements PasswordReader {

    @Override
    public String readPassword() {
        return new String(System.console().readPassword(PASSWORD_PROMPT));
    }
}
