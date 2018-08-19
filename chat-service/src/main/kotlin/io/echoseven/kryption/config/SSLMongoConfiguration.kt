package io.echoseven.kryption.config

import com.mongodb.MongoClient
import com.mongodb.MongoClientOptions
import com.mongodb.MongoCredential
import com.mongodb.ServerAddress
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.mongo.MongoProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.data.mongodb.config.AbstractMongoConfiguration
import javax.net.ssl.SSLContext

@Configuration
@Profile("cloud")
class SSLMongoConfiguration : AbstractMongoConfiguration() {

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
            MongoClientOptions.builder()
                .sslEnabled(true)
                .sslContext(SSLContext.getDefault())
                .build()
        )
    }

    @Bean
    override fun getDatabaseName(): String = mongoProperties.database
}
