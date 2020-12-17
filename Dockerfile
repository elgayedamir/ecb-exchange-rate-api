FROM adoptopenjdk:11-jre-hotspot as builder
WORKDIR exchange-rate-service
ARG JAR_FILE=target/exchange-rate-service-*.jar
COPY ${JAR_FILE} exchange-rate-service.jar
# Extracts the jar layers
RUN java -Djarmode=layertools -jar exchange-rate-service.jar extract

FROM adoptopenjdk:11-jre-hotspot
WORKDIR exchange-rate-service
# Copies the jar layers; each is copied into a different docker image layer; 
# When making changes to the app only changed layers will be rebuilt
COPY --from=builder exchange-rate-service/dependencies/ ./
COPY --from=builder exchange-rate-service/spring-boot-loader/ ./
COPY --from=builder exchange-rate-service/snapshot-dependencies/ ./
COPY --from=builder exchange-rate-service/application/ ./
ENTRYPOINT ["java", "org.springframework.boot.loader.JarLauncher"]