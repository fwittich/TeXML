package de.fwittich.texml.process;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Component;

import de.fwittich.texml.OutputFormat;

@Component
public class WindowsTeXProcessProvider extends AbstractTeXProcessProvider {

	private static final String CMD_EXE = "cmd.exe";
	private static final String CMD_OPTION = "/c";

	public Process startProcess(List<OutputFormat> outputFormats, File templateFile, File outputDirectory, String outputFileName) throws IOException {
		
		return new ProcessBuilder(CMD_EXE, CMD_OPTION, buildTeXCommands(outputFormats, templateFile, outputDirectory, outputFileName)) //
				.inheritIO() //
				.directory(templateFile.getParentFile())
				.start();		
	}
}
