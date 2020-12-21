package de.scalable.microservices.exchangerate.model;

import java.net.URI;

public class InteractiveExchangeRate extends CurrencyPair {

	private URI interactiveChartUri;
	
	public InteractiveExchangeRate(String base, String currency, URI interactiveChartUri) {
		super(base, currency);
		this.interactiveChartUri = interactiveChartUri;
	}

	public URI getInteractiveChartUri() {
		return interactiveChartUri;
	}

	public void setInteractiveChartUri(URI interactiveChartUri) {
		this.interactiveChartUri = interactiveChartUri;
	}
	
}
