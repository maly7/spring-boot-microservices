FROM java:8
WORKDIR /app
ENV TRUSTSTORE_PASSWORD secret
ENV KEYSTORE_PASSWORD secret
ADD build/libs/api-gateway-0.3.0.jar /app
ADD entrypoint.sh /
ENTRYPOINT ["/entrypoint.sh"]
