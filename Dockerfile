#
# Build stage
#
FROM maven:3.8.4-openjdk-17 AS build
COPY . .
RUN mvn clean package -Pprod -DskipTests

#
# Package stage
#
FROM openjdk:17.0.2-jdk-slim
COPY --from=build ./target/tourguide-server-1.0.jar /usr/src/app/tourguide-server-1.0.jar
WORKDIR /usr/src/app
# ENV PORT=8085
EXPOSE 8085
ENTRYPOINT ["java","-jar","tourguide-server-1.0.jar"]
