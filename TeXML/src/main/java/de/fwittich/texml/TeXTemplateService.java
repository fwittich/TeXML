package de.fwittich.texml;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import de.fwittich.texml.process.TeXProcessProvider;
import de.fwittich.texml.template.TeXTemplateProvider;


@Service
public class TeXTemplateService {
	
	private static final String VARIABLES_STRING_FORMAT = "\\def \\%s{%s} %n"; // \def \myCommand{Value}
	private static final String VARIABLES_FILENAME = "Variables.tex";
	
	@Autowired
	private TeXProcessProvider processProvider;
	
	@Autowired
	private TeXTemplateProvider templateProvider;
	
	@Value("${application.tex.outputDirectory}")
	private File outputBaseDirectory;


	public void produceFiles(List<OutputFormat> outputFormats, Map<String, String> variables, String outputFolderName) throws IOException {		
		final File outputDirectory = new File(outputBaseDirectory, outputFolderName);
						
		try {
			createVariablesFile(variables, outputDirectory);
			Process process = processProvider.startProcess(outputFormats, templateProvider.getTemplate(), outputDirectory);
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
			.forEach(entry -> 
				printWriter.printf(VARIABLES_STRING_FORMAT, entry.getKey(), entry.getValue()));
		printWriter.close();
	}



}
