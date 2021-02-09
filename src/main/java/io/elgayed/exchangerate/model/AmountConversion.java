package io.elgayed.exchangerate.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"label", "publishedAt", "amount", "result"})
public class AmountConversion extends ExchangeRate {
	
	private BigDecimal amount;
	
	public AmountConversion(LocalDate publishedAt, String base, String currency, BigDecimal amount, BigDecimal result) {
		super(publishedAt, base, currency, result);
		this.amount = amount;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	
}
