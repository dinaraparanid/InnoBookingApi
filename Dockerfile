# Replace `19` with your project's java version
FROM gradle:8-jdk19 AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN export GRADLE_OPTS="-Djdk.lang.Process.launchMechanism=vfork"
RUN gradle buildFatJar --no-daemon

# Replace `19` with your project's java version
FROM openjdk:19-alpine

RUN apk add --no-cache bash

RUN mkdir /app
COPY --from=build /home/gradle/src/build/libs/*.jar /app/InnoBookingFakeApi.jar
COPY gaydb-309f8-firebase-adminsdk-va4vn-aef993aeb9.json gaydb-309f8-firebase-adminsdk-va4vn-aef993aeb9.json

EXPOSE 8080

# Start point in your app
# In my case it was not required
# ENTRYPOINT ["java", "-jar", "/app/InnoBookingFakeApi.jar"]