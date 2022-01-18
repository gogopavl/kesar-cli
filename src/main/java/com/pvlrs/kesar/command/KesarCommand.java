package com.pvlrs.kesar.command;

import lombok.Data;
import org.springframework.stereotype.Component;
import picocli.CommandLine.Command;
import picocli.CommandLine.Model.CommandSpec;
import picocli.CommandLine.Spec;

import java.io.PrintWriter;
import java.util.concurrent.Callable;

import static com.pvlrs.kesar.constant.KesarCliConstants.KESAR_CLI_VERSION;
import static com.pvlrs.kesar.constant.KesarCliExitCodes.SUCCESS_CODE;
import static com.pvlrs.kesar.constant.ConsoleMessages.KESAR_CLI_BANNER;

@Data
@Component
@Command(
        name = "kesar",
        description = "CLI tool for encrypting and decrypting data",
        version = KESAR_CLI_VERSION,
        mixinStandardHelpOptions = true,
        subcommands = { EncryptSubCommand.class, DecryptSubCommand.class }
)
public class KesarCommand implements Callable<Integer> {

    @Spec
    private CommandSpec spec;

    public Integer call() {
        System.out.println(KESAR_CLI_BANNER);
        PrintWriter out = this.spec.commandLine().getOut();
        this.spec.commandLine().usage(out);
        return SUCCESS_CODE;
    }
}
