package com.pvlrs.kesar.exception.handler;

import picocli.CommandLine;
import picocli.CommandLine.IParameterExceptionHandler;
import picocli.CommandLine.Model.CommandSpec;
import picocli.CommandLine.ParameterException;
import picocli.CommandLine.UnmatchedArgumentException;

import java.io.PrintWriter;

import static java.util.Objects.nonNull;

public class KesarCliUserInputErrorHandler implements IParameterExceptionHandler {

    @Override
    public int handleParseException(ParameterException exception, String[] strings) throws Exception {
        CommandLine commandLine = exception.getCommandLine();
        PrintWriter errorWriter = commandLine.getErr();

        errorWriter.println(commandLine.getColorScheme().errorText(exception.getMessage()));
        UnmatchedArgumentException.printSuggestions(exception, errorWriter);
        errorWriter.print(commandLine.getHelp().fullSynopsis());

        CommandSpec spec = commandLine.getCommandSpec();
        errorWriter.printf("Try '%s --help' for more information.%n", spec.qualifiedName());

        return nonNull(commandLine.getExitCodeExceptionMapper()) ?
                commandLine.getExitCodeExceptionMapper().getExitCode(exception) : spec.exitCodeOnInvalidInput();
    }
}