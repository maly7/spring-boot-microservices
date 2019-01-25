package com.github.maly7.config

import com.mongodb.MongoClient
import com.mongodb.MongoClientOptions
import com.mongodb.MongoCredential
import com.mongodb.ServerAddress
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.autoconfigure.mongo.MongoProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.config.AbstractMongoConfiguration

@Configuration
@ConditionalOnProperty(value = ["mongodb.basic-auth"], havingValue = "true", matchIfMissing = false)
class BasicAuthMongoConfig : AbstractMongoConfiguration() {

    @Autowired
    lateinit var mongoProperties: MongoProperties

    @Bean
    override fun mongoClient(): MongoClient {
        return MongoClient(
            listOf(ServerAddress(mongoProperties.host, mongoProperties.port)),
            MongoCredential.createCredential(
                mongoProperties.username,
                mongoProperties.authenticationDatabase,
                mongoProperties.password
            ),
            MongoClientOptions.builder().build()
        )
    }

    @Bean
    override fun getDatabaseName() = mongoProperties.database
}
