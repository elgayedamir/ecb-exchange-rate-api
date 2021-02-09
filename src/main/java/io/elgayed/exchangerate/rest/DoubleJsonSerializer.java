package io.elgayed.exchangerate.rest;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;

import org.springframework.boot.jackson.JsonComponent;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

//TODO: to consider: 
// - rounding doubles at service level: the service might be injected and needed else where and extra precision is needed
// - rounding double via a JsonSerializer (this) will apply to double values returned by all rest controllers in this application
/**
 * Custom Json Double Serializer to round all double values returned by the exchange rate REST API to 4 decimal digits for consistency 
 */
@JsonComponent
public class DoubleJsonSerializer extends JsonSerializer<BigDecimal> {
	
	@Override
	public void serialize(BigDecimal value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
		gen.writeNumber(value.setScale(4, RoundingMode.CEILING));
	}
}