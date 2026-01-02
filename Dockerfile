# ===== Build stage =====
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /workspace
COPY pom.xml .
COPY src ./src
RUN mvn -q -DskipTests package

# ===== Runtime stage =====
FROM eclipse-temurin:21-jre
WORKDIR /app

# Good practics: no-root user
RUN useradd -r -u 1001 appuser
USER appuser

COPY --from=build /workspace/target/*.jar app.jar

EXPOSE 8080

# JVM setup friendly in containers (not hardcode)
ENV JAVA_OPTS="\
-XX:MaxRAMPercentage=75.0 \
-XX:InitialRAMPercentage=25.0 \
-XX:+UseG1GC \
-Djava.security.egd=file:/dev/./urandom"

ENTRYPOINT ["sh","-c","java $JAVA_OPTS -jar /app/app.jar"]
