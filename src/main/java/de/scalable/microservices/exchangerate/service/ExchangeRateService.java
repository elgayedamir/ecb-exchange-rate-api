package de.scalable.microservices.exchangerate.service;

import java.net.URI;
import java.time.LocalDate;
import java.util.Map;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ExchangeRateService {
	
	private static final String INTERACTIVE_EXCHANGE_RATE_CHART_URI = "https://www.xe.com/currencycharts/?from=%s&to=%s&view=1Y";
	
	@Autowired
	private CurrencyExchangeRateDS dataSource;
	
	public Map<String, Long> getSupportedCurrencies() {
		return dataSource.getCurrencies();
	}
	
	public Double getExchangeRate(String currency) {
		Double rate = dataSource.getCurrencyExchangeRate(currency.toUpperCase());
		if (Objects.isNull(rate))
			throw new IllegalArgumentException(String.format("The requested currency '%s' is not valid or is not supported", currency));
		return rate;
	}
	
	public Double getExchangeRate(String baseCurrency, String currency) {
		Double rate = getExchangeRate(currency.toUpperCase());
		Double baseCurrencyRate = getExchangeRate(baseCurrency.toUpperCase());
		return baseCurrencyRate/rate;
	}
	
	public URI generateInteractiveExchangeRateLink(String baseCurrency, String currency) {
		getExchangeRate(currency);
		getExchangeRate(baseCurrency);
		return URI.create(String.format(INTERACTIVE_EXCHANGE_RATE_CHART_URI, baseCurrency, currency));
	}
	
	
	public Double convert(Double amount, String currency, String baseCurrency) {
		Double rate = getExchangeRate(baseCurrency, currency);
		return rate * amount;
	}
	
	public LocalDate getPublishDate () {
		return dataSource.getPublishedAt();
	}
}
