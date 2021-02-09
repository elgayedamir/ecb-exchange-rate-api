package io.elgayed.exchangerate.service;

import java.math.BigDecimal;
import java.math.MathContext;
import java.net.URI;
import java.time.LocalDate;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.elgayed.exchangerate.model.AmountConversion;
import io.elgayed.exchangerate.model.ExchangeRate;
import io.elgayed.exchangerate.model.InteractiveExchangeRate;

@Service
public class ExchangeRateService {
	
	private static final String INTERACTIVE_EXCHANGE_RATE_CHART_URI = "https://www.xe.com/currencycharts/?from=%s&to=%s&view=1Y";
	
	@Autowired
	private CurrencyRatesDS dataSource;
	
	public Set<String> getSupportedCurrencies() {
		return dataSource.getCurrencies();
	}
	
	public BigDecimal getExchangeRate(String currency) {
		BigDecimal rate = dataSource.getCurrencyExchangeRate(currency.toUpperCase());
		return rate;
	}
	
	public ExchangeRate getExchangeRate(String baseCurrency, String currency) {
		BigDecimal rate = getExchangeRate(currency.toUpperCase());
		BigDecimal baseCurrencyRate = getExchangeRate(baseCurrency.toUpperCase());
		return new ExchangeRate(getPublishDate(), baseCurrency, currency, rate.divide(baseCurrencyRate, MathContext.DECIMAL64));
	}
	
	public InteractiveExchangeRate generateInteractiveExchangeRateLink(String baseCurrency, String currency) {
		getExchangeRate(currency);
		getExchangeRate(baseCurrency);
		return new InteractiveExchangeRate(baseCurrency, currency, URI.create(String.format(INTERACTIVE_EXCHANGE_RATE_CHART_URI, baseCurrency, currency)));
	}
	
	
	public AmountConversion convert(Double amount, String baseCurrency, String currency) {
		ExchangeRate rate = getExchangeRate(baseCurrency, currency);
		BigDecimal preciseAmount = new BigDecimal(amount);
		return new AmountConversion(getPublishDate(), baseCurrency, currency, preciseAmount, rate.getResult().multiply(preciseAmount));
	}
	
	public LocalDate getPublishDate () {
		return dataSource.getPublishedAt();
	}
}
