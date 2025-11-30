FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

COPY . .

RUN chmod +x mvnw

RUN ./mvnw dependency:go-offline

RUN ./mvnw package -DskipTests

EXPOSE 5000

CMD ["java", "-jar", "target/*.jar"]
