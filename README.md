## Getting Started
1. If running windows, make sure you install and use 64-bit java
1. Install [docker](https://docs.docker.com/) for your platform
1. Install kubernetes cli
1. Enable Kubernetes support for docker
1. Use docker for desktop as the kuberntes context: `kubectl config use-context docker-for-desktop`
1. Install the cert-manager
    1. Create the CA key `openssl genrsa -out /path/to/project/deploy/charts/cert-issuer/ca.key 2048`
    1. Create the Self Signed cert `openssl req -x509 -new -nodes -key ca.key -subj "/CN=Kryption Root CA" -days 3650 -reqexts v3_req -extensions v3_ca -out /path/to/project/deploy/charts/cert-issuer/ca.crt`
1. Prepare the cluster by running `./gradlew :d:installLocalDeps`
1. Copy `setup/template.gradle.properties` into the root project directory as `gradle.properties`
1. Generate RSA key to be used by JWT 
1. Add values for the properties in the template file, they can be anything since they'll be set for the databases when we run docker
1. Run `./gradlew :d:g:genConfigs` once to generate secrets (NOTE: if you ever change these you'll need to delete them from your k8s cluster and re-add them)
1. Only when secrets change, run `./gradlew :d:localSecrets`
1. To deploy the whole stack to your local k8s cluster run `./gradlew :d:deployLocal`
1. To reload any app after the cluster is lunched run the `reload` task for that project
1. To run just the authentication service run `./gradlew :a-s:bootRun` this will start a mysql container and then bootRun the relational store. To stop the mysql container run `./gradlew stopMysql`
1. To run just the chat service run `./gradlew :c-s:bootRun` this will start a mongo db container and then bootRun the document store. To stop the mongo db container run `./gradlew stopMongo`


### Functional Tests
The functional-tests project contains a set of api-based functional tests written using [Rest Assured](https://github.com/rest-assured/rest-assured/). These tests should be run periodically and before every merge. In the future this will be part of the CI build.

To run the functional test suite run `./gradlew :f-t:integTest`.

If you need to run against a different server rather than localhost, then specify systemProp.funcTestUri to be that host

### Generating RSA Key for JWT
1. `openssl genrsa -out authentication-service/src/main/resources/keys/private_key.pem 2048`
1. `openssl pkcs8 -topk8 -inform PEM -outform DER -in authentication-service/src/main/resources/keys/private_key.pem -out authentication-service/src/main/resources/keys/private_key.der -nocrypt`
1. `openssl rsa -in authentication-service/src/main/resources/keys/private_key.pem -pubout -outform DER -out authentication-service/src/main/resources/keys/public_key.der`

### Publishing Docker Images
Our custom docker images are built and published to the gitlab docker repository after each master build. But should you need to publish images do the following:
1. run `docker login registry.gitlab.com/maly7/kryption-api/ -u $username`
1. enter your gitlab credentials at the prompts
1. ` docker build -t registry.gitlab.com/maly7/kryption-api/chat-service:$version ./chat-service`
1. `docker build -t registry.gitlab.com/maly7/kryption-api/authentication-service:$version ./authentication-service/`
1. `docker push registry.gitlab.com/maly7/kryption-api/authentication-service`
1. `docker push registry.gitlab.com/maly7/kryption-api/chat-service`

### Running Tests
1. The chat-service tests require a 64-bit version of java to run the embedded MongoDB
