package io.elgayed.exchangerate.model;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"label", "publishedAt", "amount", "result"})
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
