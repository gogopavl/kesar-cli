package com.pvlrs.kesar.command;

import com.pvlrs.kesar.command.arguments.InputArguments;
import com.pvlrs.kesar.crypto.EncryptionService;
import com.pvlrs.kesar.utility.PasswordReaderService;
import lombok.Data;
import org.springframework.stereotype.Component;
import picocli.CommandLine.ArgGroup;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.util.concurrent.Callable;

import static com.pvlrs.kesar.constant.KesarCliConstants.KESAR_CLI_VERSION;
import static com.pvlrs.kesar.constant.KesarCliExitCodes.SUCCESS_CODE;

@Data
@Component
@Command(
        name = "encrypt",
        description = "Encrypt data.",
        version = KESAR_CLI_VERSION,
        mixinStandardHelpOptions = true
)
public class EncryptSubCommand implements Callable<Integer> {

    @ArgGroup(exclusive = true, multiplicity = "1")
    private MutuallyExclusiveArguments argumentGroup;

    @Data
    private static class MutuallyExclusiveArguments {

        @Option(
                names = {"-d", "--data"},
                description = "Raw data to be encrypted",
                paramLabel = "<data>",
                required = true
        )
        private String data;

        @Option(
                names = {"-f", "--file"},
                description = "Path to file to be encrypted",
                paramLabel = "<path-to-file>",
                required = true
        )
        private String pathToFile;
    }

    @Option(
            names = {"-p", "--password"},
            description = "Password used to encrypt data with",
            paramLabel = "<password>",
            arity = "0..1",
            interactive = true
    )
    private char[] passwordCharacters;

    private final PasswordReaderService passwordReaderService;
    private final EncryptionService encryptionService;

    public EncryptSubCommand(PasswordReaderService passwordReaderService, EncryptionService encryptionService) {
        this.passwordReaderService = passwordReaderService;
        this.encryptionService = encryptionService;
    }

    public Integer call() {
        String password = passwordReaderService.parsePasswordIfAbsentOrElseGet(passwordCharacters);
        InputArguments inputArguments = InputArguments.builder()
                .password(password)
                .data(argumentGroup.data)
                .pathToFile(argumentGroup.pathToFile).build();
        String encryptedData = encryptionService.encryptData(inputArguments);
        System.out.println("Encrypted data: " + encryptedData);
        return SUCCESS_CODE;
    }
}
