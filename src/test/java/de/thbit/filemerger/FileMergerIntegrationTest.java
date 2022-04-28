package de.thbit.filemerger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FileMergerIntegrationTest {
	private static final File OUTPUT_FILE = new File("./target/output.txt");
	private FileMerger instance = new FileMerger();

	@BeforeEach
	void deleteOutputFileIfItExists() {
		if (OUTPUT_FILE.exists()) {
			if (!OUTPUT_FILE.delete()) {
				throw new RuntimeException("Could not remove file");
			}
		}
	}

	@Test
	void mergeFileContentsSkipEachFirstLine_shouldReturnStreamWithAllLinesExceptEachFirstLine() throws Exception {
		// arrange
		final Collection<File> inputFiles = inputFiles();
		final Collection<String> expectedResult = expectedResult();

		// act
		final Stream<String> result = instance.mergeFileContentsSkipEachFirstLine(inputFiles);

		// assert
		assertEquals(expectedResult, result.collect(Collectors.toList()));
	}

	@Test
	void mergeFileContentsToFileSkipEachFirstLine_shouldCreateOutputFile() throws Exception {
		// arrange
		final Collection<File> inputFiles = inputFiles();

		// act
		instance.mergeFileContentsToFileSkipEachFirstLine(inputFiles, OUTPUT_FILE);

		// assert
		assertTrue(OUTPUT_FILE.exists());
	}

	@Test
	void mergeFileContentsToFileSkipEachFirstLine_shouldCreateOutputFileWithCorrectContent() throws Exception {
		// arrange
		final Collection<File> inputFiles = inputFiles();
		final File outputFile = new File("./target/output.txt");
		final Collection<String> expectedResult = expectedResult();

		// act
		instance.mergeFileContentsToFileSkipEachFirstLine(inputFiles, outputFile);

		// assert
		assertEquals(expectedResult, Files.readAllLines(outputFile.toPath()));
	}

	@Test
	void mergeFileContentsToFileSkipEachFirstLine_shouldOverrideExistingOutputFile() throws Exception {
		// arrange
		// act
		mergeFileContentsToFileSkipEachFirstLine_shouldCreateOutputFileWithCorrectContent();
		mergeFileContentsToFileSkipEachFirstLine_shouldCreateOutputFileWithCorrectContent();

		// assert
	}


	private Collection<String> expectedResult() throws URISyntaxException, IOException {
		final URI expectedResultUri = this.getClass().getResource("/filemerger/expectedOutput.txt").toURI();
		return Files.readAllLines(Paths.get(expectedResultUri));
	}

	private Collection<File> inputFiles() throws URISyntaxException, IOException {
		final URI inputFilesDirectory = this.getClass().getResource("/filemerger/input").toURI();
		return Files.list(Paths.get(inputFilesDirectory))
			.map(path -> path.toFile())
			.collect(Collectors.toList());
	}

}
