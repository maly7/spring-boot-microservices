FROM java:8
WORKDIR /app
ADD build/libs/document-store-1.0-SNAPSHOT.jar /app
ENTRYPOINT ["java", "-Xmx2000m", "-Djava.security.egd=file:/dev/./urandom", "-jar", "document-store-1.0-SNAPSHOT.jar"]
EXPOSE 8280
