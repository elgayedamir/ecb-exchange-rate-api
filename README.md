# Exchange Rate Service

## Overview
This service exposes a REST API for currencies exchange rates / conversion based on the reference exchange rate published daily by the European Central Bank.
For more details, check [Requirements.md](./Requirements.md)

## Details
Uses spring-batch to fetch the reference exchange rates published by the European Central Bank as a zipped CSV file, data is fetched on application
startup then every day at 16:30 (data is published  by ECB daily at 16:00).  
Data about exchange rates and fetch batch jobs are maintained in memory.

## REST API
Terminology:   
* A pair of currency USD/EUR reads 1 USD = ??? EUR  
* The first currency in the pair is the <b>base currency</b>  
* ECB publishes a reference exchange rate against EUR as base; <u>ECB reference exchange rate for USD is <b>EUR/USD</b></u>  

### List of supported currencies

```
GET /currency
```

### Exchange rate
```
GET /currency/exchange?base={base}&currency={currency}
```
* `base` [Double], [optional: defaults to `EUR`]
* `currency` [Double], [required]

Example: 
* Exchange rate from USD/GBP:  
`GET /currency/exchange?base=USD&currency=EUR`
* ECB reference Exchange rate of USD (same as EUR/USD):  
`GET /currency/exchange?base=EUR&currency=USD` | `GET /currency/exchange?currency=USD`

Response:

```
{
  "label": "EUR/USD",
  "publishedAt": "2020-12-18",
  "result": 1.2259
}
```

### Convert
Converts a given amount from a currency to another: 15 USD = ??? GBP  

```
GET /currency/convert?from={from}&to={to}&amount={amount}
```
* `from` [required] : base currency
* `to` [optional: defaults to `EUR`] : target currency
* `amount` [required] : amount to convert

Response:

```
{
  "label": "USD/EUR",
  "publishedAt": "2020-12-18",
  "amount": 1,
  "result": 0.8157
}
```

#### Interactive chart
```
GET /currency/exchange/interactive?base={base}&currency={currency}
```
* `base` [Double], [optional: defaults to `EUR`]
* `currency` [Double], [required]

## Build

Running the following maven command builds the spring boot application and generates an uber JAR

```
./mvnw clean package
```
Builds a cloud native docker image using `buildpacks`

```
./mvnw spring-boot:build-image
```
Builds a docker image using `docker engine`

```
docker build -t scalable.de/microservices/exchange-rate-service:0.0.1-SNAPSHOT .
```

## Run
* Using Maven
  
```
./mvnw spring-boot:run
```

* Running JAR after running build command (form <b>Build</b> section)

```
java -jar <PATH_TO>/exchange-rate-service.jar
```

* Running docker image

```
docker run -p 8080:8080 --name exchange-rate-service-container scalable.de/microservices/exchange-rate-service:0.0.1-SNAPSHOT
```