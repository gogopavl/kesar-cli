package com.pvlrs.kesar.utility;

import com.pvlrs.kesar.condition.StandardInputPasswordReaderCondition;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Component;

import java.io.Console;
import java.util.Scanner;

import static com.pvlrs.kesar.constant.UserMessages.PASSWORD_PROMPT;

/**
 * <p> Ideally, the password should be read via {@link Console#readPassword(java.lang.String,
 * java.lang.Object...)} as done in the {@link ConsolePasswordReader} implementation, but
 * if there isn't any {@link Console} object associated with the current JVM, this won't
 * work as {@link System#console()} will return {@code null} (for instance, when running
 * through an IDE). </p>
 *
 * <p> This implementation is offered as an alternative to {@link ConsolePasswordReader} so
 * that we are able to still read a password from the standard input. The main drawback with
 * this method is that the password is visible in the console.</p>
 */
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
