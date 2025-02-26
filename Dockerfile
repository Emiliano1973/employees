# 🔹 Fase 1: Costruzione dell'applicazione con Maven
FROM maven:3.8.3-eclipse-temurin-17 AS build

WORKDIR /app

# 🔹 Copia il pom.xml e scarica le dipendenze
COPY pom.xml .
RUN mvn dependency:go-offline -B

# 🔹 Copia il codice sorgente e costruisci l'app
COPY src ./src
RUN mvn clean package -DskipTests && rm -rf ~/.m2

# 🔹 Fase 2: Immagine finale con JRE leggero
FROM eclipse-temurin:17-jre-alpine


RUN apk add --no-cache msttcorefonts-installer fontconfig

RUN update-ms-fonts

# 🔹 Evitiamo di eseguire come root per sicurezza
RUN addgroup -S appgroup && adduser -S appuser -G appgroup
USER appuser

WORKDIR /usr/local/bin/

# 🔹 Copiamo solo il file JAR generato
COPY --from=build /app/target/*.jar webapp.jar

# 🔹 Esportiamo la porta
EXPOSE 8080

# 🔹 Consentiamo di passare il profilo come variabile d'ambiente (default: prod)
CMD ["java", "-jar", "-Dspring.profiles.active=prod", "webapp.jar"]