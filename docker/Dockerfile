FROM openjdk
RUN mkdir "/app"
WORKDIR /app
COPY ./target/WoCserver-1.0-SNAPSHOT-jar-with-dependencies.jar /app/server.jar
COPY ./src/main/log4j.xml /app/logback.xml
