# Exchange Rate Service

## Overview
This service exposes a REST API for currencies exchange rates / conversion based on the reference exchange rate published daily by the European Central Bank.
For more details, check [Requirements.md](./Requirements.md)


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