package com.pvlrs.kesar.command;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.util.concurrent.Callable;

import static com.pvlrs.kesar.constants.KesarCliConstants.KESAR_CLI_VERSION;

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

    @Option(
            names = {"-r", "--raw-key"},
            description = "Raw private key value to decrypt with.",
            paramLabel = "<key>"
    )
    private String key;

    @Option(
            names = {"-k", "--key"},
            description = "Path to file holding the private key to decrypt with.",
            paramLabel = "<path-to-key>"
    )
    private String pathToKey;

    @Option(
            names = {"-d", "--data"},
            description = "Raw data to be decrypted.",
            paramLabel = "<data>"
    )
    private String rawData;

    @Option(
            names = {"-f", "--file"},
            description = "Path to file to be decrypted.",
            paramLabel = "<path-to-file>"
    )
    private String pathToFile;

    public Integer call() {
        System.out.println("Decrypt sub-command - raw data: " + rawData);
        return 0;
    }
}
