package de.scalable.microservices.exchangerate.util;

public class PairLabel {
	
	public static final String CURRENCY_PAIR_LABEL_FORMAT = "%s/%s";
	
	public static String from (String base, String currency) {
		return String.format(CURRENCY_PAIR_LABEL_FORMAT, base, currency);
	}
	
}
