package de.scalable.microservices.exchangerate.rest;

import java.io.IOException;
import java.text.DecimalFormat;

import org.springframework.boot.jackson.JsonComponent;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

//TODO: to be considered, because this will apply to double returned by all rest endpoints in this application
/**
 * Custom Json Double Serializer to round all double values returned by the exchange rate REST API to 4 decimal digits for consistency 
 */
@JsonComponent
public class DoubleJsonSerializer extends JsonSerializer<Double> {
	
	private static final String JSON_DOUBLE_FORMAT_PATTERN = "#.####";
	
	@Override
	public void serialize(Double value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
		gen.writeNumber(new DecimalFormat(JSON_DOUBLE_FORMAT_PATTERN).format(value));
	}
}