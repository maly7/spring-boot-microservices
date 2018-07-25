#!/bin/bash

keytool -import -trustcacerts -file /trusts/tls.crt -keystore trusts.jks -storepass $TRUSTSTORE_PASS -alias "Kryption Root CA" -noprompt
openssl pkcs12 -export -in /certs/tls.crt -inkey /certs/tls.key -CAfile /trusts/tls.crt -name "service-registry" -passout pass:$KEYSTORE_PASS -out server.p12
java -Xmx2000m -Djava.security.egd=file:/dev/./urandom -Dspring.profiles.active=cloud -jar /app/service-registry-0.1.7.jar