package de.fwittich.texml;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class TeXTemplateService {
	
	private static final String CMD_EXE = "cmd.exe";
	private static final String CMD_OPTION = "/c";

	private static final String VARIABLES_STRING_FORMAT = "\\def \\%s{%s}"; // \def \myCommand{Value}
	private static final String VARIABLES_FILENAME = "Variables.tex";
	
	@Value("${application.tex.outputDirectory}")
	private String outputDirectory;
	
	@Value("${application.tex.templateDirectory}")
	private String templateDirectory;


	public void produceFiles(String template, Map<String, String> variables, String outputFolder) throws IOException {
		final File outputDirectoryFile = new File(outputDirectory, outputFolder);
		
		ProcessBuilder processBuilder = 
			new ProcessBuilder(CMD_EXE, CMD_OPTION, buildTeXCommands(template, outputDirectoryFile)) //
			.inheritIO() //
			.directory(new File(getClass().getResource(templateDirectory).getFile()));
		
		try {
			createVariablesFile(variables, outputDirectoryFile);
			Process process = processBuilder.start();
			int exitVal = process.waitFor();
			if (exitVal == 0) {
				System.out.println("Success!");

			} else {
				System.out.println("Failure");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void createVariablesFile(Map<String, String> variables, File outputDirectory) throws IOException {
		File file = new File(outputDirectory, VARIABLES_FILENAME);
		file.getParentFile().mkdirs();
		
		FileWriter fileWriter = new FileWriter(file);
		PrintWriter printWriter = new PrintWriter(fileWriter);
		variables.entrySet().stream()
			.forEach(entry -> printWriter.printf(VARIABLES_STRING_FORMAT, entry.getKey(), entry.getValue()));
		printWriter.close();
	}
	
	private String buildTeXCommands(String fileName, File outputDirectory) {
		String commands = Stream.of( //			
				"latex "+fileName+".tex -aux-directory="+outputDirectory.getAbsolutePath()+" -output-directory="+outputDirectory.getAbsolutePath(), //
				"cd " + outputDirectory.getAbsolutePath(), //
				"dvips "+fileName+".dvi", //
				"ps2pdf "+fileName+".ps" //
		)//
				.collect(Collectors.joining(" & "));
		return commands;
	}

}
