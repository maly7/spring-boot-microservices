#!/bin/bash

keytool -import -trustcacerts -file /trusts/tls.crt -keystore trusts.jks -storepass $TRUSTSTORE_PASSWORD -alias "Kryption Root CA" -noprompt
openssl pkcs12 -export -in /certs/tls.crt -inkey /certs/tls.key -CAfile /trusts/tls.crt -name "service-registry" -passout pass:$KEYSTORE_PASSWORD -out server.p12
exec java $JAVA_OPTS -Djavax.net.ssl.trustStorePassword=$TRUSTSTORE_PASSWORD -jar /app/service-registry-0.2.0.jar
