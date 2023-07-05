# Replace `17` with your project's java version
FROM gradle:7-jdk17 AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle buildFatJar --no-daemon

# Replace `17` with your project's java version
FROM openjdk:17-alpine

# Replace with port in your project
EXPOSE 8080:8080

RUN apk add --no-cache bash

RUN mkdir /app
COPY --from=build /home/gradle/src/build/libs/*.jar /app/

# Start point in your app
# In my case it was not required
ENTRYPOINT ["java","-jar","/app/InnoBookingFakeApi-all.jar"]