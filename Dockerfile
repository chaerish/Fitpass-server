FROM openjdk:17

ARG JAR_FILE=build/libs/*.jar

COPY ${JAR_FILE} fitpass-server.jar

ENTRYPOINT ["java" ,"-Dspring.profiles.active=${SPRING_PROFILES_ACTIVE}","-jar", "/fitpass-server.jar"]