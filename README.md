## Getting Started
1. Install docker and docker-compose for your platform
1. Copy `setup/template.gradle.properties` into the root project directory
1. Generate RSA key to be used by JWT 
1. Add values for the properties in the template file, they can be anything since they'll be set for the databases when we run docker
1. To run the full stack run `./gradlew run`, then to stop it `./gradlew stop`
1. To run just the authentication service run `./gradlew :a-s:bootRun` this will start a mysql container and then bootRun the relational store. To stop the mysql container run `./gradlew stopMysql`
1. To run just the chat service run `./gradlew :c-s:bootRun` this will start a mongo db container and then bootRun the document store. To stop the mongo db container run `./gradlew stopMongo`

### Functional Tests
The functional-tests project contains a set of api-based functional tests written using [Rest Assured](https://github.com/rest-assured/rest-assured/). These tests should be run periodically and before every merge. In the future this will be part of the CI build.

To run the functional tests, first start the entire application with `./gradlew run`. Once everything is up an running run `./gradlew :f-t:integTest`. 

### Generating RSA Key
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
