package io.echoseven.kryption.data

import io.echoseven.kryption.domain.User
import org.springframework.data.mongodb.repository.MongoRepository

interface UserRepository : MongoRepository<User, String>
