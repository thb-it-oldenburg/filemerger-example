package de.thbit.filemerger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.stream.Collectors;

public class FileMergerApplication {
	public static void main(String[] args) throws IOException {
		final Collection<File> filesToMerge = Files.list(Paths.get(retrieveSourcePath(args)))
			.map(path -> path.toFile())
			.filter(file -> !file.isDirectory())
			.filter(file -> file.canRead())
			.filter(file -> file.getName().endsWith(".txt"))
			.collect(Collectors.toList());
		new FileMerger().mergeFileContentsSkipEachFirstLine(filesToMerge);
	}

	private static String retrieveSourcePath(String[] args) {
		if (args != null && args.length > 0) {
			return args[0];
		}
		return ".";
	}
}
