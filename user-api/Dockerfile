FROM java:8
WORKDIR /app
ADD build/libs/user-api-1.0-SNAPSHOT.jar /app
ENTRYPOINT ["java", "-Xmx2000m", "-Djava.security.egd=file:/dev/./urandom", "-jar", "user-api-1.0-SNAPSHOT.jar"]
EXPOSE 8180
