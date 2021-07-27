package de.fwittich.texml;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TeXMLApplication implements ApplicationRunner  {

	@Autowired
	private TeXTemplateService texTemplateService;
	
	public static void main(String[] args) {
		SpringApplication.run(TeXMLApplication.class, args);
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {
		
		String outputFolderName = getOutputFolderName(args);
		Map<String, String> variables = getVariables(args);
		
		texTemplateService.produceFiles("Template01", variables, outputFolderName);
	}

	private Map<String, String> getVariables(ApplicationArguments args) {
		List<String> variableOptions = args.getOptionValues("variable");
		
		return variableOptions.stream()
			.filter(variable -> variable.contains(":"))
			.map(variable -> variable.split(":"))
			.collect(Collectors.toMap(s -> s[0], s -> s[1]));
	}
	
	private String getOutputFolderName(ApplicationArguments args) {
		List<String> docOptions = args.getOptionValues("DOC");

		if(docOptions == null || docOptions.isEmpty() || docOptions.size() > 1) {
			System.exit(0);
		}
		
		return docOptions.get(0);
	}

}
