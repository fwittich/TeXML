package de.fwittich.texml.template;

import java.io.File;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class DummyTeXTemplateProvider implements TeXTemplateProvider {

	private static final String DUMMY_TEMPLATE = "Template01.tex";
	
	@Value("${application.tex.templateDirectory}")
	private String templateDirectory;

	@Override
	public File getTemplate() {
		File f = new File(getClass().getResource(templateDirectory).getFile());
		return new File(f, DUMMY_TEMPLATE);
	}
	
}
