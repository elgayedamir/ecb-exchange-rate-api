package io.elgayed.exchangerate.batch;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;

/**
 * Custom implementation for an ItemReader that reads zipped CSV file resources.
 * Extracts files from the zip in a tmp directory and sets them as a {@link Resource} for the {@link FlatFileItemReader} 
 */
public class ZipFileItemReader<T> extends FlatFileItemReader<T> {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ZipFileItemReader.class);
	
	private Resource archive;
	private Path tmpDirectory;

	@Override
	public void open(ExecutionContext executionContext) throws ItemStreamException {
		
		if (Objects.isNull(archive)) //Preconditions
			throw new IllegalStateException("An archive resource needs to be provided");

		try {
			//Create tmp directory to be used for unzipping the archive;
			tmpDirectory = Files.createTempDirectory("ecb_daily_echange_rates");
			
			//unzip
			try (ZipInputStream zipIn = new ZipInputStream(archive.getInputStream())) {
				for (ZipEntry zipEntry; (zipEntry = zipIn.getNextEntry()) != null;) {
					Path unzippedFilePath = tmpDirectory.resolve(zipEntry.getName());
					Files.copy(zipIn, unzippedFilePath, StandardCopyOption.REPLACE_EXISTING);
					this.setResource(new UrlResource(unzippedFilePath.toUri()));
				}
			}
		} catch (IOException e) {
			throw new RuntimeException(String.format("An error occured while unzipping the archived resource '%s'", archive.getDescription()), e);
		}

		super.open(executionContext);
	}

	@Override
	public void close() throws ItemStreamException {
		super.close();
		try {
			FileUtils.forceDelete(tmpDirectory.toFile());
		} catch (IOException e) {
			LOGGER.debug(String.format("Could not delete tmp directory '%s' used for archived resource '%s', Directory is set to be cleaned on JVM exit", 
					this.tmpDirectory.toAbsolutePath(), this.archive.getDescription()), e);
			tmpDirectory.toFile().deleteOnExit();
		}
	}

	public void setArchive(Resource archive) {
		this.archive = archive;
	}

}