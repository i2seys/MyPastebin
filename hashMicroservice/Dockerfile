# Build
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Run
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
# Копируем собранный jar.
COPY --from=build /app/target/hashMicroservice-*.jar app.jar

# Передаем переменные окружения в Java
ENV SPRING_PROFILES_ACTIVE=docker

EXPOSE 8091
ENTRYPOINT ["java", "-jar", "app.jar"]