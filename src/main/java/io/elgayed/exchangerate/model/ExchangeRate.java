package io.elgayed.exchangerate.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ExchangeRate extends CurrencyPair{
	
	private LocalDate publishedAt;
	private BigDecimal result;
	
	public ExchangeRate(LocalDate publishedAt, String base, String currency, BigDecimal result) {
		super(base, currency);
		this.publishedAt = publishedAt;
		this.result = result;
	}

	public LocalDate getPublishedAt() {
		return publishedAt;
	}

	public void setPublishedAt(LocalDate publishedAt) {
		this.publishedAt = publishedAt;
	}

	public BigDecimal getResult() {
		return result;
	}

	public void setResult(BigDecimal result) {
		this.result = result;
	}
}
