package de.scalable.microservices.exchangerate.service;

import java.net.URI;
import java.time.LocalDate;
import java.util.Map;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.scalable.microservices.excahngerate.model.AmountConversion;
import de.scalable.microservices.excahngerate.model.ExchangeRate;
import de.scalable.microservices.excahngerate.model.InteractiveExchangeRate;

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
	
	public ExchangeRate getExchangeRate(String baseCurrency, String currency) {
		Double rate = getExchangeRate(currency.toUpperCase());
		Double baseCurrencyRate = getExchangeRate(baseCurrency.toUpperCase());
		return new ExchangeRate(getPublishDate(), baseCurrency, currency, rate/baseCurrencyRate);
	}
	
	public InteractiveExchangeRate generateInteractiveExchangeRateLink(String baseCurrency, String currency) {
		getExchangeRate(currency);
		getExchangeRate(baseCurrency);
		return new InteractiveExchangeRate(baseCurrency, currency, URI.create(String.format(INTERACTIVE_EXCHANGE_RATE_CHART_URI, baseCurrency, currency)));
	}
	
	
	public AmountConversion convert(Double amount, String baseCurrency, String currency) {
		ExchangeRate rate = getExchangeRate(baseCurrency, currency);
		return new AmountConversion(getPublishDate(), baseCurrency, currency, amount, rate.getResult() * amount);
	}
	
	public LocalDate getPublishDate () {
		return dataSource.getPublishedAt();
	}
}
