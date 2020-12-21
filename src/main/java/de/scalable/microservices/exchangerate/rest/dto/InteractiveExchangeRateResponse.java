package de.scalable.microservices.exchangerate.rest.dto;

import java.net.URI;
import java.time.LocalDate;

public class InteractiveExchangeRateResponse extends ApiResponse {

	private URI interactiveChartUri;
	
	public InteractiveExchangeRateResponse(LocalDate publishedAt, String label, URI interactiveChartUri) {
		super(publishedAt, label);
		this.interactiveChartUri = interactiveChartUri;
	}

	public URI getInteractiveChartUri() {
		return interactiveChartUri;
	}

	public void setInteractiveChartUri(URI interactiveChartUri) {
		this.interactiveChartUri = interactiveChartUri;
	}
	
}
