FROM java:8
WORKDIR /app
ADD build/libs/api-gateway-1.0-SNAPSHOT.jar /app
ENTRYPOINT ["java", "-Xmx2000m", "-Djava.security.egd=file:/dev/./urandom", "-Dspring.profiles.active=cloud", "-jar", "api-gateway-1.0-SNAPSHOT.jar"]
EXPOSE 8080
