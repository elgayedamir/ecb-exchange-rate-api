package de.scalable.microservices.exchangerate.service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("singleton")
public class CurrencyExchangeRateDS {
	
	public static final String EURO_CURRENCY_SYMBOL = "EUR";
	
	private LocalDate publishedAt;
	private Map<String, Double> currencyRates = new ConcurrentHashMap<>();
	private Map<String, Long> currencyRequestsCount = new ConcurrentHashMap<>();
	
	public void updateCurrencyRates (Map<String, Double> currencyRates, LocalDate publishTime) {
		this.publishedAt = publishTime;
		this.currencyRates.putAll(currencyRates);
		this.currencyRates.put(EURO_CURRENCY_SYMBOL, 1.0);
		this.currencyRates.keySet().stream().forEach(currency -> currencyRequestsCount.putIfAbsent(currency, 0L));
	}
	
	/**
	 * This method, given a currency, returns its exchange rate against euro (base currency)
	 * @param currency to fetch its exchange rate
	 * @return the exchange rate of the given currency against euro
	 */
	public Double getCurrencyExchangeRate (String currency) {
		Double rate = this.currencyRates.get(currency);
		currencyRequestsCount.merge(currency, 1L, Long::sum);
		return rate;
	}
	
	/**
	 * Returns supported currencies and how many times they were requested
	 * @return a map having as key the supported currencies and as value how many times each currency was requested
	 */
	public Map<String, Long> getCurrencies() {
		return Collections.unmodifiableMap(this.currencyRequestsCount);
	}
	
	/**
	 * @return The {@link Date} this data was published by the European Central Bank
	 */
	public LocalDate getPublishedAt() {
		return this.publishedAt;
	}
}
