FROM openjdk:17

COPY target/backend-0.0.1-SNAPSHOT.jar ./app.jar

ENTRYPOINT ["java", "org.springframework.boot.loader.launch.JarLauncher"]

CMD ["java", "-jar", "./app.jar"]
