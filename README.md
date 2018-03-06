## Getting Started
1. Install docker and docker-compose for your platform
1. Copy `setup/template.gradle.properties` into the root project directory
1. Add values for the properties in the template file, they can be anything since they'll be set for the databases when we run docker
1. To run the full stack run `./gradlew run`, then to stop it `./gradlew stop`
1. To run just the relational store run `./gradlew :r-s:bootRun` this will start a mysql container and then bootRun the relational store. To stop the mysql container run `./gradlew stopMysql`
1. To run just the document store run `./gradlew :d-s:bootRun` this will start a mongo db container and then bootRun the document store. To stop the mongo db container run `./gradlew stopMongo`


### Publishing Docker Images
Our custom docker images are built and published to the gitlab docker repository after each master build. But should you decide to publish an image locally from a Windows machine you'll need to disable TLS for the docker daemon by going to docker > settings > General > Check "Expose daemon on tcp://localhost:2375 without TLS". 