package de.scalable.capital.microservices.exchangerateservice.service;

import java.util.Collections;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("singleton")
public class CurrencyExchangeRateDS {
	
	private Date publishedAt;
	private Map<String, Double> currenciesRate = new ConcurrentHashMap<>();
	private Map<String, Long> currenciesRequestsCount = new ConcurrentHashMap<>();
	
	public void updateCurrencyRates (Map<String, Double> currencyRates, Date publishTime) {
		this.publishedAt = publishTime;
		this.currenciesRate.putAll(currencyRates);
		this.currenciesRate.put("EUR", 1.0);
		this.currenciesRate.keySet().stream().forEach(currency -> currenciesRequestsCount.put(currency, 0L));
	}
	
	/**
	 * This method, given a currency, returns its exchange rate against euro (base currency)
	 * @param currency to fetch its exchange rate
	 * @return the exchange rate of the given currency against euro
	 */
	public Double getCurrencyExchangeRate (String currency) {
		Double rate = this.currenciesRate.get(currency);
		currenciesRequestsCount.merge(currency, 1L, Long::sum);
		return rate;
	}
	
	/**
	 * Returns supported currencies and how many times they were requested
	 * @return a map having as key the supported currencies and as value how many times each currency was requested
	 */
	public Map<String, Long> getCurrencies() {
		return Collections.unmodifiableMap(this.currenciesRequestsCount);
	}
	
	/**
	 * @return The {@link Date} this data was published by the European Central Bank
	 */
	public Date getPublishedAt() {
		return this.publishedAt;
	}
}
