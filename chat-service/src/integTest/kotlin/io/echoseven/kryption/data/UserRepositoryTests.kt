package io.echoseven.kryption.data

import io.echoseven.kryption.ChatIntegrationTest
import io.echoseven.kryption.domain.User
import org.hamcrest.CoreMatchers.hasItem
import org.hamcrest.Matchers.empty
import org.hamcrest.Matchers.hasSize
import org.junit.After
import org.junit.Assert.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.mongodb.core.MongoOperations
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query
import org.springframework.test.context.junit4.SpringRunner
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

@RunWith(SpringRunner::class)
@ChatIntegrationTest
class UserRepositoryTests {

    @Autowired
    lateinit var userRepository: UserRepository

    @Autowired
    lateinit var mongoTemplate: MongoTemplate

    @Autowired
    lateinit var mongoOperations: MongoOperations

    @After
    fun cleanup() {
        userRepository.deleteAll()
    }

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

    @Test
    fun `update an existing user`() {
        val originalUser = userRepository.save(User("email@foo.com"))
        val newImageUrl = "https://image.com"
        val newUserName = "Test User"
        originalUser.profileImageUrl = newImageUrl
        originalUser.name = newUserName

        val updatedUser = userRepository.save(originalUser)

        assertEquals(newImageUrl, updatedUser.profileImageUrl)
        assertEquals(newUserName, updatedUser.name)

        val users = mongoOperations.query(User::class).inCollection("users").all()
        assertThat("The created user should exist in the users collection", users, hasItem(updatedUser))
        assertThat("The users collection should have only one document", users, hasSize(1))
    }

    @Test
    fun `delete an existing user`() {
        val user = userRepository.save(User("user@deleteme.com"))

        assertThat("The user should exist in the db", userRepository.findAll(), hasItem(user))

        userRepository.delete(user)
        assertThat("The user should be deleted", userRepository.findAll(), empty())
    }
}
