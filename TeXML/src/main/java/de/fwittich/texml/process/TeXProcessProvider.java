package de.fwittich.texml.process;

import java.io.File;
import java.io.IOException;
import java.util.List;

import de.fwittich.texml.OutputFormat;

public interface TeXProcessProvider {
	
	default Process startProcess(List<OutputFormat> outputFormats, File templateFile, File outputDirectory) throws IOException {
		return startProcess(outputFormats, templateFile, outputDirectory, outputDirectory.getName());
	}
	
	public Process startProcess(List<OutputFormat> outputFormats, File templateFile, File outputDirectory, String outputFileName) throws IOException;

}
