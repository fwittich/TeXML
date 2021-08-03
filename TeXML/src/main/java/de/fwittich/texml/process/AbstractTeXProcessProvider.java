package de.fwittich.texml.process;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import de.fwittich.texml.OutputFormat;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbstractTeXProcessProvider implements TeXProcessProvider {
	
	private static final String LATEX_COMMAND_FORMAT = "latex %s -aux-directory=%s -output-directory=%s -jobname=%s";
	
	private static final String PDFLATEX_COMMAND_FORMAT = "pdflatex %s -aux-directory=%s -output-directory=%s -jobname=%s";
	
	private static final String DVIPS_COMMAND_FORMAT = "dvips %s -o %s";
	
	protected String buildTeXCommands(List<OutputFormat> outputFormats, File templateFile, File outputDirectory, String outputFileName) {
		List<String> commandList = new ArrayList<>();

		String outpath = outputDirectory.getAbsolutePath();
		String templateFileName = templateFile.getName();

		if (outputFormats.contains(OutputFormat.DVI) || outputFormats.contains(OutputFormat.PS)) {
			commandList.add( //
					String.format(LATEX_COMMAND_FORMAT, //
							templateFileName, //
							outpath, //
							outpath, //
							outputFileName //
							) 
			);
		}

		if (outputFormats.contains(OutputFormat.PS)) {
			commandList.add( //
					String.format(DVIPS_COMMAND_FORMAT, //
							new File(outputDirectory, outputFileName + ".dvi").getAbsolutePath(), //
							new File(outputDirectory, outputFileName + ".ps").getAbsolutePath()) //
			);
		}

		if (outputFormats.contains(OutputFormat.PDF)) {
			commandList.add(String.format(PDFLATEX_COMMAND_FORMAT, //
					templateFileName, //
					outpath, //
					outpath, //
					outputFileName //
					)
			);
		}
		
		String commands = commandList.stream().collect(Collectors.joining(" & "));
		
		log.debug(commands);

		return commands;
	}

}
