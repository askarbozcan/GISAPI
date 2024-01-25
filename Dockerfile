FROM maven:3.9.6-eclipse-temurin-17 as build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

FROM eclipse-temurin:17
WORKDIR /app
COPY --from=build /app/target/GISAPI-1.0-SNAPSHOT-jar-with-dependencies.jar /app
EXPOSE 7123
COPY ./test_jsons /app/test_jsons
CMD ["java", "-jar", "GISAPI-1.0-SNAPSHOT-jar-with-dependencies.jar", "--jsons-path", "/app/test_jsons"]



