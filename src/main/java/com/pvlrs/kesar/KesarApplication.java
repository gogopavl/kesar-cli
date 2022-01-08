package com.pvlrs.kesar;

import com.pvlrs.kesar.command.KesarCommand;
import com.pvlrs.kesar.exception.handler.KesarCliErrorHandler;
import com.pvlrs.kesar.exception.handler.KesarCliUserInputErrorHandler;
import com.pvlrs.kesar.exception.mapper.KesarCliExitCodeExceptionMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.ExitCodeGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import picocli.CommandLine;
import picocli.CommandLine.IFactory;

@SpringBootApplication
public class KesarApplication implements CommandLineRunner, ExitCodeGenerator {

	private final IFactory factory;
	private final KesarCommand kesarCommand;

	private int exitCode;

	public KesarApplication(IFactory factory, KesarCommand kesarCommand) {
		this.factory = factory;
		this.kesarCommand = kesarCommand;
	}

	@Override
	public void run(String[] args) {
		this.exitCode = new CommandLine(kesarCommand, factory)
				.setParameterExceptionHandler(new KesarCliUserInputErrorHandler())
				.setExecutionExceptionHandler(new KesarCliErrorHandler())
				.setExitCodeExceptionMapper(new KesarCliExitCodeExceptionMapper())
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
