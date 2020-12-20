package de.scalable.capital.microservices.exchangerateservice.rest.dto;

import java.time.LocalDate;

public abstract class ApiResponse {
	
	private LocalDate publishedAt;
	private String label;
	
	public ApiResponse(LocalDate publishedAt, String label) {
		super();
		this.publishedAt = publishedAt;
		this.label = label;
	}

	public LocalDate getPublishedAt() {
		return publishedAt;
	}

	public void setPublishedAt(LocalDate publishedAt) {
		this.publishedAt = publishedAt;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}
	
}
