FROM java:8
WORKDIR /app
ENV TRUSTSTORE_PASS secret
ENV KEYSTORE_PASS secret
ADD build/libs/authentication-service-0.1.11.jar /app
ADD entrypoint.sh /
ENTRYPOINT ["/entrypoint.sh"]
EXPOSE 8180
