## Getting Started
1. If running windows, make sure you install and use 64-bit java
1. Install [docker](https://docs.docker.com/) for your platform
1. Configure docker to allow more ram and cpu usage (at least 4GB of ram)
1. Install kubernetes cli
1. Install helm
1. Enable Kubernetes support for docker
1. Use docker for desktop as the kubernetes context: `kubectl config use-context docker-for-desktop`
1. Initialize helm/tiller `helm init`
1. Install the cert-manager
    1. Create the CA key `openssl genrsa -out /path/to/project/deploy/charts/cert-issuer/ca.key 2048`
    1. Create the Self Signed cert `openssl req -x509 -new -nodes -key ca.key -subj "/CN=Demo App Root CA" -days 3650 -reqexts v3_req -extensions v3_ca -out /path/to/project/deploy/charts/cert-issuer/ca.crt`
1. Copy `setup/template.gradle.properties` into the root project directory as `gradle.properties`
1. Generate RSA key to be used by JWT 
1. Add values for the properties in the template file, they can be anything since they'll be set for the databases when we run docker
1. To deploy the whole stack to your local k8s cluster run `./gradlew :d:deployLocal`
1. To reload any app after the cluster is lunched run the `reload` task for that project
1. To run just the authentication service run `./gradlew :a-s:bootRun` this will start a mysql container and then bootRun the relational store. To stop the mysql container run `./gradlew stopMysql`
1. To run just the chat service run `./gradlew :c-s:bootRun` this will start a mongo db container and then bootRun the document store. To stop the mongo db container run `./gradlew stopMongo`

### Functional Tests
The functional-tests project contains a set of api-based functional tests written using [Rest Assured](https://github.com/rest-assured/rest-assured/). These tests should be run periodically and before every merge. In the future this will be part of the CI build.

To run the functional test suite run `./gradlew :f-t:integTest`.

If you need to run against a different server rather than localhost, then specify systemProp.funcTestUri to be that host

Make sure to setup a truststore for functional tests by:
1. Generate the truststore: `keytool -import -trustcacerts -file deploy/charts/cert-issuer/ca.crt -keystore trusts.jks -storepass $STORE_PASS -alias "Demo App Root CA" -noprompt`
1. Specify the location and password for the truststore using gradle properties

### Generating RSA Key for JWT
1. `openssl genrsa -out authentication-service/src/main/resources/keys/private_key.pem 2048`
1. `openssl pkcs8 -topk8 -inform PEM -outform DER -in authentication-service/src/main/resources/keys/private_key.pem -out authentication-service/src/main/resources/keys/private_key.der -nocrypt`
1. `openssl rsa -in authentication-service/src/main/resources/keys/private_key.pem -pubout -outform DER -out authentication-service/src/main/resources/keys/public_key.der`

### Running Tests
1. The chat-service tests require a 64-bit version of java to run the embedded MongoDB
