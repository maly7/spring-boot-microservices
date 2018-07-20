## Getting Started
1. If running windows, make sure you install and use 64-bit java
1. Install docker and docker-compose for your platform
1. Install minikube for your platform (here's a good guide for [windows](https://medium.com/@JockDaRock/minikube-on-windows-10-with-hyper-v-6ef0f4dc158c)
1. Configure minikube to use local docker-env (on windows run `minikube docker-env` and follow the directions on macOS/linux run `eval $(minikube docker-env)`)
1. Copy `setup/template.gradle.properties` into the root project directory as `gradle.properties`
1. Generate RSA key to be used by JWT 
1. Add values for the properties in the template file, they can be anything since they'll be set for the databases when we run docker
1. Run `./gradlew :d:g:genConfigs` once to generate secrets (NOTE: if you ever change these you'll need to delete them from your k8s cluster and re-add them)
1. Only when secrets change, run `./gradlew :d:localSecrets`
1. To deploy the whole stack to your minikube run `./gradlew :d:deployLocal`
1. To reload any app after the cluster is lunched run the `reload` task for that project
1. To run just the authentication service run `./gradlew :a-s:bootRun` this will start a mysql container and then bootRun the relational store. To stop the mysql container run `./gradlew stopMysql`
1. To run just the chat service run `./gradlew :c-s:bootRun` this will start a mongo db container and then bootRun the document store. To stop the mongo db container run `./gradlew stopMongo`


### Functional Tests
The functional-tests project contains a set of api-based functional tests written using [Rest Assured](https://github.com/rest-assured/rest-assured/). These tests should be run periodically and before every merge. In the future this will be part of the CI build.

Make sure you have minikube up and running and your gradle property `systemProp.funcTestUri` set to the minikube service url `./gradlew :f-t:integTest`.

To get the right funcTestUri run `minikube service api-gateway-lb --url` and set it to that output 

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

### Helpful Minikube commands
* `minikube start` and `minikube stop` to start/stop the cluster
* `minikube service $SERVICE_NAME --url` to get the addressable url for an exposed service

### Minikube on windows
* Currently we need to use version 0.27 so we can use the start/stop commands on the cluster.
* When first launching a cluster you'll need to stop it, then turn off dynamic memory management 
