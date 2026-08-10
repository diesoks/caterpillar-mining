# ---- Build stage ----
# Uses the project's own Maven Wrapper (mvnw), so the exact Maven version pinned in
# .mvn/wrapper/maven-wrapper.properties is used, with no separate Maven installation needed.
FROM eclipse-temurin:25-jdk AS build
WORKDIR /app

COPY .mvn/ .mvn/
COPY mvnw pom.xml ./
RUN chmod +x mvnw
# Warms the local Maven repository cache in its own layer, so this step is skipped on
# subsequent builds unless pom.xml actually changes.
RUN ./mvnw -B dependency:go-offline

COPY src ./src
RUN ./mvnw -B clean package -DskipTests

# ---- Runtime stage ----
FROM eclipse-temurin:25-jre
WORKDIR /app
COPY --from=build /app/target/backend-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
