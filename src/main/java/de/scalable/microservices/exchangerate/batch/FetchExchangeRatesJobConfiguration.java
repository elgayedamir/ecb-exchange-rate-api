package de.scalable.microservices.exchangerate.batch;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileParseException;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.PassThroughLineMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.UrlResource;

/**
 * Configuration for a batch job that downloads the currency exchange rates published by the European Central Bank.
 * <br>
 * Data is published as a zipped CSV file available for download under "https://www.ecb.europa.eu/stats/eurofxref/eurofxref.zip"
 */
@Configuration
@EnableBatchProcessing
public class FetchExchangeRatesJobConfiguration {
	
	private static final String FETCH_CURRENCY_RATE_BATCH_JOB_NAME = "fetch_currency_rate_job";
	private static final String FETCH_CURRENCY_RATE_STEP_NAME = "fetch_currency_rate_step";
	private static final int STEP_CHUNK_SIZE = 2;
	private static final URI ECB_DAILY_CSV_EXCHANGE_RATES_URL = URI.create("https://www.ecb.europa.eu/stats/eurofxref/eurofxref.zip");
	
	@Bean
	public Job fetchCurrencyRateJob(JobBuilderFactory jobBuilderFactory, Step step) {
		return jobBuilderFactory
				.get(FETCH_CURRENCY_RATE_BATCH_JOB_NAME)
				.flow(step)
				.end()
				.build();
	}
	
	@Bean
	public Step fetchCurrencyRatesStep(
			StepBuilderFactory stepBuilderFactory, 
			ItemReader<String> itemReader, 
			ItemWriter<String> itemWriter) {
		
		return stepBuilderFactory.get(FETCH_CURRENCY_RATE_STEP_NAME)
				.<String, String>chunk(STEP_CHUNK_SIZE)
				.reader(itemReader)
				.writer(itemWriter)
				.faultTolerant()
				.skip(FlatFileParseException.class)
				.skipLimit(2)
				//retry when failed to acquire the remote CSV file
				.retry(IOException.class)
				.retryLimit(5)
				.build();
	}
	
	@Bean
	public ItemReader<String> exchaneRatesCSVFileReader(LineMapper<String> mapper) throws IOException {
		ZipFileItemReader<String> reader = new ZipFileItemReader<>();
		reader.setArchive(new UrlResource(ECB_DAILY_CSV_EXCHANGE_RATES_URL));
		reader.setLineMapper(mapper);
		reader.setEncoding(StandardCharsets.UTF_8.name());
		reader.setStrict(Boolean.FALSE);
		return reader;
	}

	@Bean
	public LineMapper<String> lineMapper() {
		return new PassThroughLineMapper();
	}
	
}
