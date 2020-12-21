package de.scalable.microservices.excahngerate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder("label")
public abstract class CurrencyPair {
	
	public static final String CURRENCY_PAIR_LABEL_FORMAT = "%s/%s";
	
	private String base;
	private String currency;
	
	public CurrencyPair(String base, String currency) {
		this.base = base;
		this.currency = currency;
	}

	@JsonIgnore
	public String getBase() {
		return base;
	}

	public void setBase(String base) {
		this.base = base;
	}
	
	@JsonIgnore
	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}
	
	public String getLabel() {
			return String.format(CURRENCY_PAIR_LABEL_FORMAT, base, currency);
	}
	
}
