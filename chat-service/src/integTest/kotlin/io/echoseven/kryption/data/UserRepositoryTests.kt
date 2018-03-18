package io.echoseven.kryption.data

import io.echoseven.kryption.domain.User
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
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
}