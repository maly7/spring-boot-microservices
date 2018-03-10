FROM java:8
WORKDIR /app
ADD build/libs/conversation-api-1.0-SNAPSHOT.jar /app
ENTRYPOINT ["java", "-Xmx2000m", "-Djava.security.egd=file:/dev/./urandom", "-jar", "conversation-api-1.0-SNAPSHOT.jar"]
EXPOSE 8280
