package de.scalable.capital.microservices.exchangerateservice.rest.dto;

import java.time.LocalDate;

public class ExchangeRateConversionResponse extends ExchangeRateResponse {
	
	private Double amount;
	
	public ExchangeRateConversionResponse(LocalDate publishedAt, String label, Double result, Double amount) {
		super(publishedAt, label, result);
		this.amount = amount;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}
}
