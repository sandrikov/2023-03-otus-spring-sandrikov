FROM bellsoft/liberica-openjdk-alpine-musl:17.0.2-9 AS books-app
COPY /target/books-docker-1.0.jar /app/books-docker-1.0.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/books-docker-1.0.jar"]
