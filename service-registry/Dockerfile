FROM java:8
WORKDIR /app
ADD build/libs/service-registry-1.0-SNAPSHOT.jar /app
ENTRYPOINT ["java", "-Xmx2000m", "-Djava.security.egd=file:/dev/./urandom", "-Dspring.profiles.active=cloud", "-jar", "service-registry-1.0-SNAPSHOT.jar"]
