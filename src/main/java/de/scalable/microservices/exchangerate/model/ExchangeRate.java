package de.scalable.microservices.exchangerate.model;

import java.time.LocalDate;

public class ExchangeRate extends CurrencyPair{
	
	private LocalDate publishedAt;
	private Double result;
	
	public ExchangeRate(LocalDate publishedAt, String base, String currency, Double result) {
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

	public Double getResult() {
		return result;
	}

	public void setResult(Double result) {
		this.result = result;
	}
}
