FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

COPY target/family-wishlist-app-0.2.jar app.jar

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]