package com.pvlrs.kesar.utility;

import com.pvlrs.kesar.condition.StandardInputPasswordReaderCondition;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Component;

import java.util.Scanner;

import static com.pvlrs.kesar.constant.UserMessages.PASSWORD_PROMPT;

@Component
@Conditional(StandardInputPasswordReaderCondition.class)
public class StandardInputPasswordReader implements PasswordReader {

    @Override
    public String readPassword() {
        System.out.print(PASSWORD_PROMPT);
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine();
    }
}
