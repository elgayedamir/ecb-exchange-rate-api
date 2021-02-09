package io.elgayed.exchangerate.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Date;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("singleton")
public class CurrencyRatesDS {
	
	public static final String EURO_CURRENCY_SYMBOL = "EUR";
	
	private LocalDate publishedAt;
	private Map<String, BigDecimal> currencyRates = new ConcurrentHashMap<>();
	
	public void updateCurrencyRates (Map<String, BigDecimal> currencyRates, LocalDate publishTime) {
		this.publishedAt = publishTime;
		this.currencyRates.putAll(currencyRates);
		this.currencyRates.put(EURO_CURRENCY_SYMBOL, BigDecimal.ONE);
	}
	
	/**
	 * This method, given a currency, returns its exchange rate against euro (base currency)
	 * @param currency to fetch its exchange rate
	 * @return the exchange rate of the given currency against euro
	 * @throws IllegalArgumentException if the given currency is not supported
	 */
	public BigDecimal getCurrencyExchangeRate (String currency) {
		BigDecimal rate = this.currencyRates.get(currency);
		if (Objects.isNull(rate))
			throw new IllegalArgumentException(String.format("The requested currency '%s' is not valid or is not supported", currency));
		return rate;
	}
	
	/**
	 * Returns supported currencies
	 * @return a {@link Set} of the supported currencies
	 */
	public Set<String> getCurrencies() {
		return Collections.unmodifiableSet(this.currencyRates.keySet());
	}
	
	/**
	 * @return The {@link Date} this data was published by the European Central Bank
	 */
	public LocalDate getPublishedAt() {
		return this.publishedAt;
	}
}
