package com.pvlrs.kesar.exception.handler;

import picocli.CommandLine;
import picocli.CommandLine.IExecutionExceptionHandler;
import picocli.CommandLine.ParseResult;

import static java.util.Objects.nonNull;

public class KesarCliErrorHandler implements IExecutionExceptionHandler {

    @Override
    public int handleExecutionException(Exception exception, CommandLine commandLine, ParseResult parseResult) {

        commandLine.getErr().println(commandLine.getColorScheme().errorText(exception.getMessage()));

        return nonNull(commandLine.getExitCodeExceptionMapper()) ?
                commandLine.getExitCodeExceptionMapper().getExitCode(exception) :
                commandLine.getCommandSpec().exitCodeOnExecutionException();
    }
}
