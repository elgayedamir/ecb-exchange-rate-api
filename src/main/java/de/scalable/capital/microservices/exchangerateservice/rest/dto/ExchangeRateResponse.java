package de.scalable.capital.microservices.exchangerateservice.rest.dto;

import java.time.LocalDate;

public class ExchangeRateResponse extends ApiResponse{
	

	private Double result;
	
	public ExchangeRateResponse(LocalDate publishedAt, String label, Double result) {
		super(publishedAt, label);
		this.result = result;
	}

	public Double getValue() {
		return result;
	}

	public void setValue(Double result) {
		this.result = result;
	}

}
