FROM maven:3-eclipse-temurin-21-alpine as builder
COPY . .
RUN mvn package

FROM eclipse-temurin:21
ARG JAR_FILE=target/*.jar
COPY --from=builder ${JAR_FILE} pepito-mastodon.jar
ENTRYPOINT ["java","-jar","./pepito-mastodon.jar"]
