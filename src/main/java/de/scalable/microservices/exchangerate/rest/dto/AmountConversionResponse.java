package de.scalable.microservices.exchangerate.rest.dto;

import java.time.LocalDate;

public class AmountConversionResponse extends ExchangeRateResponse {
	
	private Double amount;
	
	public AmountConversionResponse(LocalDate publishedAt, String label, Double result, Double amount) {
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
