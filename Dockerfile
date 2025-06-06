FROM maven:3.9.0-eclipse-temurin-17 AS builder

WORKDIR /build

COPY pom.xml ./

RUN mvn dependency:go-offline -B

COPY src ./src

RUN mvn clean package -DskipTests -B

FROM amazoncorretto:17

WORKDIR /bank-card

COPY --from=builder /build/target/bank-card-*.jar ./app.jar

COPY ./entrypoint.sh ./entrypoint.sh

RUN chmod +x ./entrypoint.sh

ENTRYPOINT ["./entrypoint.sh"]
