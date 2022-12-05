FROM openjdk:17-jdk-slim
COPY build/libs/server-side-0.0.1-SNAPSHOT.jar /app.jar

CMD ["--server.port=8080"]

EXPOSE 8080
EXPOSE 7001

ENTRYPOINT ["java","-jar","/app.jar"]