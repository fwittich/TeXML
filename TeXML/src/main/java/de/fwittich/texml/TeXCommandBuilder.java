package de.fwittich.texml;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class TeXCommandBuilder {

	private static final String LATEX_COMMAND_FORMAT = "latex %s.tex -aux-directory=%s -output-directory=%s";
	private static final String PDFLATEX_COMMAND_FORMAT = "pdflatex %s.tex -aux-directory=%s -output-directory=%s";
	private static final String DVIPS_COMMAND_FORMAT = "dvips %s -o %s";

	public String buildTeXCommands(List<OutputFormat> outputFormats, String fileName, File outputDirectory) {
		List<String> commandList = new ArrayList<>();

		String outpath = outputDirectory.getAbsolutePath();

		if (outputFormats.contains(OutputFormat.DVI) || outputFormats.contains(OutputFormat.PS)) {
			commandList.add( //
					String.format(LATEX_COMMAND_FORMAT, //
							fileName, //
							outpath, //
							outpath) //
			);
		}

		if (outputFormats.contains(OutputFormat.PS)) {
			commandList.add( //
					String.format(DVIPS_COMMAND_FORMAT, //
							new File(outputDirectory, fileName + ".dvi").getAbsolutePath(), //
							new File(outputDirectory, fileName + ".ps").getAbsolutePath()) //
			);
		}

		if (outputFormats.contains(OutputFormat.PDF)) {
			commandList.add(String.format(PDFLATEX_COMMAND_FORMAT, //
					fileName, //
					outpath, //
					outpath) //
			);
		}
		
		String commands = commandList.stream().collect(Collectors.joining(" & "));
		
		log.debug(commands);

		return commands;
	}

}
