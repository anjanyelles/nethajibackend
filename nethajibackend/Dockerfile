# ---- Build stage ----
FROM maven:3.9-eclipse-temurin-17 AS build

WORKDIR /app

# Leverage Docker layer caching
COPY pom.xml ./
RUN mvn -DskipTests dependency:go-offline

COPY src ./src
RUN mvn -DskipTests package

# ---- Runtime stage ----
FROM eclipse-temurin:17-jre

WORKDIR /app

# Create a non-root user
RUN useradd -r -u 10001 appuser

# Copy the built jar
COPY --from=build /app/target/*.jar /app/app.jar

USER appuser

# Default; most platforms will override via PORT env var
ENV PORT=9029

EXPOSE 9029

# Ensure Spring Boot listens on the platform-provided port
ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS:-} -Dserver.port=${PORT:-9029} -jar /app/app.jar"]
