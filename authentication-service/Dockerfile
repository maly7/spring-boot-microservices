FROM java:8
WORKDIR /app
ADD build/libs/authentication-service-1.0-SNAPSHOT.jar /app
ENTRYPOINT ["java", "-Xmx2000m", "-Djava.security.egd=file:/dev/./urandom", "-Dspring.profiles.active=cloud", "-jar", "authentication-service-1.0-SNAPSHOT.jar"]
EXPOSE 8180
