FROM maven:3.9.2-eclipse-temurin-20-alpine AS build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src ./src
COPY resources ./resources
RUN mvn clean install -DskipTests

FROM openjdk:17-oracle
WORKDIR /app
COPY --from=build /app/target/jira-1.0.jar jira.jar
COPY src ./src
COPY resources ./resources
ENTRYPOINT ["java", "-jar", "jira.jar"]

ENV POSTGRES_USERNAME=jira
ENV POSTGRES_PASSWORD=JiraPass
ENV MAIL_USERNAME=jira4jr@gmail.com
ENV MAIL_PASSWORD=zdfzsrqvgimldzyj

EXPOSE 8083