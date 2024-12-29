FROM eclipse-temurin:23-noble AS builder

WORKDIR /src

COPY mvnw .
COPY pom.xml .

COPY src src
COPY .mvn .mvn

RUN chmod a+x ./mvnw && ./mvnw package -Dmaven.test.skip=true

FROM eclipse-temurin:23-jre-noble

WORKDIR /app

COPY --from=builder /src/target/ssf-project-0.0.1-SNAPSHOT.jar vttp-ssf-project.jar

ENV SERVER_PORT=8050

EXPOSE ${SERVER_PORT}

ENTRYPOINT java -jar vttp-ssf-project.jar
