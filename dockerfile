FROM openjdk
RUN mkdir "/app"
WORKDIR /app
ENV SERVER_IP="10.10.50.144"
ENV SERVER_PORT="9144"
COPY server.jar /app/server.jar
COPY legacy.jar /app/legacy.jar
CMD java -jar /app/server.jar
CMD java -jar /app/legacy.jar
