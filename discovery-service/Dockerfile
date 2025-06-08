FROM jelastic/maven:3.9.5-openjdk-21 AS build
WORKDIR /home/app

COPY ./pom.xml /home/app/pom.xml
COPY ./src /home/app/src

RUN mvn -f /home/app/pom.xml clean package -DskipTests

FROM amazoncorretto:21.0.4-alpine3.18
COPY --from=build /home/app/target/*.jar DiscoveryService.jar
EXPOSE 8761
ENTRYPOINT ["java", "-jar", "DiscoveryService.jar"]