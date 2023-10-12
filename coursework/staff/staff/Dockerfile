FROM bellsoft/liberica-openjdk-alpine-musl:19.0.2 AS books-app
COPY /target/staff-1.0.jar /app/staff-1.0.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/staff-1.0.jar"]
