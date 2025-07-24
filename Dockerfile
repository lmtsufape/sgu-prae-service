# syntax=docker/dockerfile:1.3
ARG BASE_IMAGE=openjdk:24-jdk-slim

FROM ${BASE_IMAGE} AS build
WORKDIR /app
# coping the Maven wrapper and configuration files
COPY mvnw ./
COPY .mvn .mvn
COPY pom.xml ./
RUN chmod +x mvnw
# Use the Maven wrapper to download dependencies and prepare the project
RUN --mount=type=cache,target=/root/.m2 \
    ./mvnw dependency:go-offline -B
# coping the source code
COPY src ./src
# Build the project using the Maven wrapper, skipping tests
RUN --mount=type=cache,target=/root/.m2 \
    ./mvnw clean install -DskipTests

FROM ${BASE_IMAGE} AS runtime
WORKDIR /app

COPY --from=build /app/target/*.jar app.jar
ENTRYPOINT ["java","-jar","app.jar"]
