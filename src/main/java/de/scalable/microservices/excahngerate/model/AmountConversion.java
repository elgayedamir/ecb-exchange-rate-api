package de.scalable.microservices.excahngerate.model;

import java.time.LocalDate;

public class AmountConversion extends ExchangeRate {
	
	private Double amount;
	
	public AmountConversion(LocalDate publishedAt, String base, String currency, Double amount, Double result) {
		super(publishedAt, base, currency, result);
		this.amount = amount;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}
	
}
