# Exchange Rate Service

## Overview
This service exposes a REST API for currencies exchange rates / conversion based on the reference exchange rate published daily by the European Central Bank.
For more details, check [Requirements.md](./Requirements.md)

## Details
Uses spring-batch to fetch the reference exchange rates published by the European Central Bank as a zipped CSV file, data is fetched on application
startup then every day at 16:30 (data is published  by ECB daily at 16:00).  
Data about exchange rates and fetch batch jobs are maintained in memory.


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