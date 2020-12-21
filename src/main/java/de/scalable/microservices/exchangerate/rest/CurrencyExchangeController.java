package de.scalable.microservices.exchangerate.rest;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import de.scalable.microservices.exchangerate.rest.dto.AmountConversionResponse;
import de.scalable.microservices.exchangerate.rest.dto.ApiError;
import de.scalable.microservices.exchangerate.rest.dto.ExchangeRateResponse;
import de.scalable.microservices.exchangerate.rest.dto.InteractiveExchangeRateResponse;
import de.scalable.microservices.exchangerate.service.CurrencyExchangeRateDS;
import de.scalable.microservices.exchangerate.service.ExchangeRateService;
import de.scalable.microservices.exchangerate.util.PairLabel;

@RestController
@RequestMapping(path = "currency")
public class CurrencyExchangeController {
	
	@Autowired
	private ExchangeRateService exchangeRateService;
	
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getSupportedCurrencies () {
		return new ResponseEntity<>(exchangeRateService.getSupportedCurrencies(), HttpStatus.OK);
	}
	
	@GetMapping(path = "/exchange/interactive", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> interactiveExchangeRateLink (
			@RequestParam(required = true) final String currency, 
			@RequestParam(defaultValue = CurrencyExchangeRateDS.EURO_CURRENCY_SYMBOL) final String base) {
		
		URI uri = exchangeRateService.generateInteractiveExchangeRateLink(base, currency);
		return new ResponseEntity<>(
				new InteractiveExchangeRateResponse(exchangeRateService.getPublishDate(), PairLabel.from(base, currency), uri)
				, HttpStatus.OK);
	}
	
	
	@GetMapping(path = "/exchange", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> exchangeRate (
			@RequestParam(required = true) final String currency, 
			@RequestParam(defaultValue = CurrencyExchangeRateDS.EURO_CURRENCY_SYMBOL) final String base) {
		
		Double rate = exchangeRateService.getExchangeRate(base, currency);
		return new ResponseEntity<>(
				new ExchangeRateResponse(exchangeRateService.getPublishDate(), PairLabel.from(base, currency), rate), 
				HttpStatus.OK);
	}
	
	@GetMapping(path = "/convert", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> convert (
			@RequestParam(required = true) final double amount, 
			@RequestParam(required = true) final String from, 
			@RequestParam(defaultValue = CurrencyExchangeRateDS.EURO_CURRENCY_SYMBOL) final String to) {
		
		Double convertedAmount = exchangeRateService.convert(amount, from, to);
		return new ResponseEntity<>(
				new AmountConversionResponse(exchangeRateService.getPublishDate(), PairLabel.from(to, from), convertedAmount, amount)
				, HttpStatus.OK);
	}
	
	@ExceptionHandler(IllegalArgumentException.class)
	@ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY)
	@ResponseBody
	protected ApiError handleIllegalArgumentException(IllegalArgumentException ex) {
		return new ApiError(HttpStatus.UNPROCESSABLE_ENTITY.name(), ex.getMessage());
	}
	
	@ExceptionHandler(MissingServletRequestParameterException.class)
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	@ResponseBody
	protected ApiError handleMissingServletRequestParameterException(MissingServletRequestParameterException ex) {
		return new ApiError(HttpStatus.BAD_REQUEST.name(), ex.getMessage());
	}
	
	/**
	 * Binds a property editor, that transforms string parameters (in our case currency symbols) to upper case
	 * This applies to this Rest Controller only
	 * @param dataBinder used for end-points in this controller
	 */
	@InitBinder
	public void initBinder( WebDataBinder dataBinder) {
	    dataBinder.registerCustomEditor( String.class, new UpperCaseEditor());
	}
}
