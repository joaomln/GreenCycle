FROM eclipse-temurin
EXPOSE 8080
ADD target/GreenCycle-0.0.1-SNAPSHOT.jar GreenCycle.jar
ENTRYPOINT ["java", "-jar", "GreenCycle.jar", "-Dspring.profiles.active=test"]