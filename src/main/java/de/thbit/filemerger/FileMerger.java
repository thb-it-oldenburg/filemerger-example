package de.thbit.filemerger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Collection;
import java.util.stream.Stream;

public class FileMerger {
	public void mergeFileContentsToFileSkipEachFirstLine(Collection<File> filesToMerge, File outputFile)
		throws IOException {

		final Stream<String> mergedFileContents = mergeFileContentsSkipEachFirstLine(filesToMerge);

		Files.write(Paths.get(outputFile.toURI()),
			streamToIterable(mergedFileContents),
			StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
	}

	private static <T> Iterable<T> streamToIterable(Stream<T> stream) {
		return stream::iterator;
	}

	public Stream<String> mergeFileContentsSkipEachFirstLine(Collection<File> filesToMerge) {
		return filesToMerge.stream()
			.map(file -> readAllLinesExceptTheFirst(file))
			.flatMap(fileContent -> fileContent);
	}

	private Stream<String> readAllLinesExceptTheFirst(File file) {
		try {
			return Files.lines(Paths.get(file.toURI()))
				.skip(1);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
