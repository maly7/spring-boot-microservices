package io.echoseven.kryption.data

import io.echoseven.kryption.domain.User
import org.hamcrest.CoreMatchers
import org.hamcrest.CoreMatchers.hasItem
import org.junit.Assert
import org.junit.Assert.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.mongodb.core.MongoOperations
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit4.SpringRunner
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

@RunWith(SpringRunner::class)
@ActiveProfiles("local", "integration")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserRepositoryTests {

    @Autowired
    lateinit var userRepository: UserRepository

    @Autowired
    lateinit var mongoTemplate: MongoTemplate

    @Autowired
    lateinit var mongoOperations: MongoOperations

    @Test
    fun `create a new user`() {
        val user = userRepository.save(User("email@foo.com"))

        assertNotNull(user.id, "The user id should be assigned")

        val foundOptional = userRepository.findById(user.id!!)
        assertTrue(foundOptional.isPresent, "The user should be found")

        val foundUser = foundOptional.get()
        assertEquals(user.email, foundUser.email)
        assertEquals(user.name, foundUser.name)
        assertEquals(user.profileImageUrl, foundUser.profileImageUrl)
    }

    @Test
    fun `users should be stored in the users collection`() {
        val user = userRepository.save(User("email@foo.com"))
        assertTrue(mongoTemplate.collectionExists("users"), "The users collection should exist")

        val users = mongoOperations.query(User::class).inCollection("users").all()

        assertThat("The created user should exist in the users collection", users, hasItem(user))
    }
}