FROM openjdk:21-jdk-slim

WORKDIR /app

COPY target/fast-food-0.0.1-SNAPSHOT.jar /app/fast-food.jar

EXPOSE 8080

CMD ["java", "-jar", "/app/fast-food.jar"]