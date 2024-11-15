FROM openjdk:17-alpine

EXPOSE 8080

RUN apk add --no-cache msttcorefonts-installer fontconfig
RUN update-ms-fonts

WORKDIR /usr/local/bin/

COPY target/demo-0.0.1-SNAPSHOT.jar webapp.jar

CMD ["java", "-jar","webapp.jar"]