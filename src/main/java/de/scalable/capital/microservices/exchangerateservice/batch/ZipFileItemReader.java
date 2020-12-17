package de.scalable.capital.microservices.exchangerateservice.batch;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;

/**
 * Custom implementation for an ItemReader that reads zip files,
 * extracts zip files in a tmp directory and uses them as {@link Resource} for the {@link FlatFileItemReader} 
 * Deletes the used intermediary files on reader close
 */
public class ZipFileItemReader<T> extends FlatFileItemReader<T> {
	
	private Resource archive;
	private List<Path> intermediateFiles = new ArrayList<>();

	@Override
	public void open(ExecutionContext executionContext) throws ItemStreamException {
		
		if (Objects.isNull(archive)) //Preconditions
			throw new IllegalStateException("An archive resource needs to be provided");

		try {
			//Create tmp directory to be used for tmp files;
			Path tmpDirectory = Files.createTempDirectory("ecb_daily_echange_rates");
			
			//unzip
			try (ZipInputStream zipIn = new ZipInputStream(archive.getInputStream())) {
				for (ZipEntry zipEntry; (zipEntry = zipIn.getNextEntry()) != null;) {
					Path unzippedFilePath = tmpDirectory.resolve(zipEntry.getName());
					Files.copy(zipIn, unzippedFilePath, StandardCopyOption.REPLACE_EXISTING);
					intermediateFiles.add(unzippedFilePath);
					this.setResource(new UrlResource(unzippedFilePath.toUri()));
				}
			}
			
			//Get intermediate file references to be deleted on close
			intermediateFiles.add(tmpDirectory);
		} catch (IOException e) {
			// TODO add meaningful message
			throw new RuntimeException(String.format("An error occured while unzipping the archived resource (%s)", archive.getDescription()), e);
		}

		super.open(executionContext);
	}

	@Override
	public void close() throws ItemStreamException {
		super.close();
		intermediateFiles.forEach(path -> path.toFile().delete());
	}

	public void setArchive(Resource archive) {
		this.archive = archive;
	}

}