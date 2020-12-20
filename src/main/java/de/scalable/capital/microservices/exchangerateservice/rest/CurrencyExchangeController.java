package de.scalable.capital.microservices.exchangerateservice.rest;

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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import de.scalable.capital.microservices.exchangerateservice.rest.dto.ApiError;
import de.scalable.capital.microservices.exchangerateservice.rest.dto.ExchangeRateConversionResponse;
import de.scalable.capital.microservices.exchangerateservice.service.CurrencyExchangeRateDS;
import de.scalable.capital.microservices.exchangerateservice.service.ExchangeRateService;

/*
 * 
 	1. I want to retrieve the ECB reference rate for a currency pair, e.g. USD/EUR or
		HUF/EUR.
	2. I want to retrieve an exchange rate for other pairs, e.g. HUF/USD.
	3. I want to retrieve a list of supported currencies and see how many times they were
		requested.
	4. I want to convert an amount in a given currency to another, e.g. 15 EUR = ??? GBP
	5. I want to retrieve a link to a public website showing an interactive chart for a given
		currency pair.
 */

//TODO consider moving from, to, amount to query params
@RestController
@RequestMapping(path = "currency")
public class CurrencyExchangeController {
	
	@Autowired
	private ExchangeRateService exchangeRateService;
	
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getSupportedCurrencies () {
		return new ResponseEntity<>(exchangeRateService.getSupportedCurrencies(), HttpStatus.OK);
	}
	
	@GetMapping(path = "/exchange/{currency}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> exchangeRateAgainstEuro (@PathVariable final String currency) {
		Double rate = exchangeRateService.getExchangeRate(currency);
		return new ResponseEntity<>(rate, HttpStatus.OK);
	}
	
	@GetMapping(path = "/exchange/{currency}/to/{baseCurrency}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> exchangeRate (@PathVariable final String currency, @PathVariable final String baseCurrency) {
		Double rate = exchangeRateService.getExchangeRate(currency, baseCurrency);
		return new ResponseEntity<>(rate, HttpStatus.OK);
	}
	
	@GetMapping(path = "/exchange/interactive/{currency}/to/{targetCurrency}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> interactiveExchangeRateLink (@PathVariable final String currency, @PathVariable final String targetCurrency) {
		URI uri = exchangeRateService.generateInteractiveExchangeRateLink(currency, targetCurrency);
		return new ResponseEntity<>(uri, HttpStatus.OK);
	}
	
	
	@GetMapping(path = "/convert/{amount}/{currency}/to/{targetCurrency}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> convert (@PathVariable final double amount, @PathVariable final String currency, @PathVariable final String targetCurrency) {
		Double convertedAmount = exchangeRateService.convert(amount, currency, targetCurrency);
		return new ResponseEntity<>(convertedAmount, HttpStatus.OK);
	}
	
	/***/
	// REST API mapping refactor
	/***/
	@GetMapping(path = "/exchange/interactive", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> interactiveExchangeRateLinkRef (
			@RequestParam(required = true) final String currency, 
			@RequestParam(defaultValue = CurrencyExchangeRateDS.EURO_CURRENCY_SYMBOL) final String base) {
		URI uri = exchangeRateService.generateInteractiveExchangeRateLink(currency, base);
		return new ResponseEntity<>(uri, HttpStatus.OK);
	}
	
	
	@GetMapping(path = "/exchange", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> exchangeRateRef (
			@RequestParam(required = true) final String currency, 
			@RequestParam(defaultValue = CurrencyExchangeRateDS.EURO_CURRENCY_SYMBOL) final String base) {
		Double rate = exchangeRateService.getExchangeRate(currency, base);
		return new ResponseEntity<>(rate, HttpStatus.OK);
	}
	
	@GetMapping(path = "/convert", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> convertRef (
			@RequestParam(required = true) final double amount, 
			@RequestParam(required = true) final String from, 
			@RequestParam(defaultValue = CurrencyExchangeRateDS.EURO_CURRENCY_SYMBOL) final String to) {
		
		System.out.println("from -> " + from);
		
		Double convertedAmount = exchangeRateService.convert(amount, from, to);
		return new ResponseEntity<>(convertedAmount, HttpStatus.OK);
	}
	
	/****/
	//Using a POJO for response
	/****/
	@Autowired
	private CurrencyExchangeRateDS ds;
	@GetMapping(path = "/convert/pojo", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> convertRefWithPojo (
			@RequestParam(required = true) final double amount, 
			@RequestParam(required = true) final String from, 
			@RequestParam(defaultValue = CurrencyExchangeRateDS.EURO_CURRENCY_SYMBOL) final String to) {
		
		Double convertedAmount = exchangeRateService.convert(amount, from, to);
		//return new ResponseEntity<>(convertedAmount, HttpStatus.OK);
		return new ResponseEntity<>(
				new ExchangeRateConversionResponse(ds.getPublishedAt(), String.format("%s/%s", from, to), convertedAmount, amount)
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
