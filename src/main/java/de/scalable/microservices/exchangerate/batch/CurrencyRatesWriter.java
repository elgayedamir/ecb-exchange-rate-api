package de.scalable.microservices.exchangerate.batch;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import de.scalable.microservices.exchangerate.service.CurrencyRatesDS;

@Component
public class CurrencyRatesWriter implements ItemWriter<String> {
	
	/**
	 * The exchange rate CSV file published by ECB contains the publishing date as first column;
	 * The following pattern is used to parse the publish date 
	 */
	private static final String CSV_EXCHANGE_RATE_PUBLISHING_DATE_FORMAT = "dd MMMM yyyy";
	
	@Autowired
	private CurrencyRatesDS dataSource;
	
	@Override
	public void write(List<? extends String> items) throws Exception {
		
		String[] keys = items.get(0).split(", ");
		String[] values = items.get(1).split(", ");
		Map<String, Double> currenciesRate = new HashMap<>();
		for (int i = 1; i < keys.length; i++)
			currenciesRate.put(keys[i], Double.valueOf(values[i]));
		
		String publishDateValue = values[0];
		LocalDate publishDate = LocalDate.parse(publishDateValue, DateTimeFormatter.ofPattern(CSV_EXCHANGE_RATE_PUBLISHING_DATE_FORMAT));
		dataSource.updateCurrencyRates(currenciesRate, publishDate);
	}
}
