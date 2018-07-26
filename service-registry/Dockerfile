FROM java:8
WORKDIR /app
ENV TRUSTSTORE_PASSWORD secret
ENV KEYSTORE_PASSWORD secret
ADD build/libs/service-registry-0.1.11.jar /app
ADD entrypoint.sh /
ENTRYPOINT ["/entrypoint.sh"]
