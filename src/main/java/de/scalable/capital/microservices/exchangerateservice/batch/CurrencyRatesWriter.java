package de.scalable.capital.microservices.exchangerateservice.batch;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import de.scalable.capital.microservices.exchangerateservice.service.CurrencyExchangeRateDS;

@Component
public class CurrencyRatesWriter implements ItemWriter<String> {

	@Autowired
	private CurrencyExchangeRateDS currencyExchangeRateDS;
	
	@SuppressWarnings("deprecation")
	@Override
	public void write(List<? extends String> items) throws Exception {
		String[] keys = items.get(0).split(", ");
		String[] values = items.get(1).split(", ");
		Map<String, Double> currenciesRate = new HashMap<>();
		for (int i = 1; i < keys.length; i++)
			currenciesRate.put(keys[i], Double.valueOf(values[i]));
		
		currencyExchangeRateDS.updateCurrencyRates(currenciesRate, new Date(values[0]));
	}
}
