FROM openjdk
RUN mkdir "/app"
WORKDIR /app
COPY ./target/server.clean-1.0-jar-with-dependencies.jar /app/server.jar
COPY ./src/main/log4j.xml /app/logback.xml