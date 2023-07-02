FROM openjdk:11

#COPY target/daotao-0.0.1-SNAPSHOT.jar daotao-0.0.1-SNAPSHOT.jar
#
# ENTRYPOINT ["java", "-jar", "daotao-0.0.1-SNAPSHOT.jar"]

WORKDIR /app

COPY .mvn/ .mvn
COPY mvnw pom.xml ./

ARG AMAZON_S3_BUCKET=__AMAZON_S3_BUCKET__
ARG AMAZON_S3_ACCESSKEY=__AMAZON_S3_ACCESSKEY__
ARG AMAZON_S3_PRIVATEKEY=__AMAZON_S3_PRIVATEKEY__

RUN chmod +x ./mvnw
RUN ./mvnw dependency:go-offline


COPY src ./src

CMD ["./mvnw", "spring-boot:run"]