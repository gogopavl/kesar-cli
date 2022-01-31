package com.pvlrs.kesar.command;

import com.pvlrs.kesar.command.arguments.InputArguments;
import com.pvlrs.kesar.crypto.DecryptionService;
import com.pvlrs.kesar.utility.PasswordService;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;
import picocli.CommandLine.ArgGroup;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.util.concurrent.Callable;

import static com.pvlrs.kesar.constant.KesarCliConstants.KESAR_CLI_VERSION;
import static com.pvlrs.kesar.constant.KesarCliExitCodes.SUCCESS_CODE;

@Setter
@Getter
@Component
@Command(
        name = "decrypt",
        description = "Decrypt data.",
        version = KESAR_CLI_VERSION,
        mixinStandardHelpOptions = true
)
public class DecryptSubCommand implements Callable<Integer> {

    @ArgGroup(exclusive = true, multiplicity = "1")
    private MutuallyExclusiveArguments argumentGroup;

    @Data
    private static class MutuallyExclusiveArguments {

        @Option(
                names = {"-d", "--data"},
                description = "Encrypted data to be decrypted",
                paramLabel = "<data>",
                required = true
        )
        private String data;

        @Option(
                names = {"-f", "--file"},
                description = "Path to encrypted file to be decrypted",
                paramLabel = "<path-to-encrypted-file>",
                required = true
        )
        private String pathToFile;
    }

    @Option(
            names = {"-p", "--password"},
            description = "Password used to decrypt data with",
            paramLabel = "<password>",
            arity = "0..1",
            interactive = true
    )
    private char[] passwordCharacters;

    private final PasswordService passwordReaderService;
    private final DecryptionService decryptionService;

    public DecryptSubCommand(PasswordService passwordService, DecryptionService decryptionService) {
        this.passwordReaderService = passwordService;
        this.decryptionService = decryptionService;
    }

    public Integer call() {
        String password = passwordReaderService.parsePasswordIfAbsentOrElseGet(passwordCharacters);
        InputArguments inputArguments = InputArguments.builder()
                .password(password)
                .data(argumentGroup.data)
                .pathToFile(argumentGroup.pathToFile).build();
        String decryptedData = decryptionService.decryptData(inputArguments);
        System.out.println("Decrypted data: " + decryptedData);
        return SUCCESS_CODE;
    }
}
