FROM openjdk:11-jdk as build
VOLUME /tmp
COPY . .
RUN ./gradlew clean build

FROM openjdk:11-jdk
WORKDIR /app
COPY --from=build build/libs/*.jar app.jar
ARG JAR_FILE
ENTRYPOINT ["java", "-jar", "app.jar"]
EXPOSE 8080

#docker build -t gmdb/monolith .
#docker run -d -p80:8080 --name monolith -eDB_HOST=gmdb-devdb --network gmdb-bridge gmdb/monolith
