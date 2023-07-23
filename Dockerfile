FROM openjdk:latest
MAINTAINER Leonardo Gomes dos Santos
WORKDIR /app
COPY target/miniautorizador-0.0.1-SNAPSHOT.jar /app/miniautorizador.jar
ENV PORT=8080
ENTRYPOINT  java -jar miniautorizador.jar
EXPOSE $PORT