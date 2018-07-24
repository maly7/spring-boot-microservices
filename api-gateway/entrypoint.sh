#!/bin/bash

keytool -import -trustcacerts -file /trusts/tls.crt -keystore trusts.jks -storepass $TRUSTSTORE_PASS -alias "Kryption Root CA" -noprompt
openssl pkcs12 -export -in /certs/tls.crt -inkey /certs/tls.key -CAfile /trusts/tls.crt -name "api-gateway" -passout pass:$KEYSTORE_PASS -out server.p12
java -Xmx2000m -Djava.security.egd=file:/dev/./urandom -Dspring.profiles.active=cloud -jar /app/api-gateway-0.1.2.jar