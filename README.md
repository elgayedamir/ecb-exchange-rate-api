# Exchange Rate Service

## Objective 
Get familiar with Spring Batch core concepts, patterns and architecture.

## Overview
This service exposes a REST API for currencies exchange rates / conversion based on the reference exchange rate published daily by the European Central Bank.
For more details, check [Requirements.md](./Requirements.md)


## Build

Running the following maven command builds the spring boot application and generates an uber JAR

```
./mvnw clean package
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