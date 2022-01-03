package com.pvlrs.kesar;

import com.pvlrs.kesar.command.KesarCommand;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.ExitCodeGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import picocli.CommandLine;

@SpringBootApplication
public class KesarApplication implements CommandLineRunner, ExitCodeGenerator {

	private final CommandLine.IFactory factory;
	private final KesarCommand kesarCommand;

	private int exitCode;

	public KesarApplication(CommandLine.IFactory factory, KesarCommand kesarCommand) {
		this.factory = factory;
		this.kesarCommand = kesarCommand;
	}

	@Override
	public void run(String[] args) {
		this.exitCode = new CommandLine(kesarCommand, factory)
//				.setParameterExceptionHandler(new CliUserInputErrorHandler())
//				.setExecutionExceptionHandler(new CliErrorHandler())
//				.setExitCodeExceptionMapper(new CliExitCodeExceptionMapper())
				.execute(args);
	}

	@Override
	public int getExitCode() {
		return this.exitCode;
	}

	public static void main(String[] args) {
		System.exit(SpringApplication.exit(SpringApplication.run(KesarApplication.class, args)));
	}
}
