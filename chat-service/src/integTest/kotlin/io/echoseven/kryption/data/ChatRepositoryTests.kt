package io.echoseven.kryption.data

import io.echoseven.kryption.ChatIntegrationTest
import io.echoseven.kryption.domain.Chat
import org.junit.After
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.junit4.SpringRunner
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

@RunWith(SpringRunner::class)
@ChatIntegrationTest
class ChatRepositoryTests {

    @Autowired
    lateinit var chatRepository: ChatRepository

    @After
    fun cleanup() {
        chatRepository.deleteAll()
    }

    @Test
    fun `create a new chat`() {
        val chat = chatRepository.save(Chat())

        assertNotNull(chat.id, "The chat should be saved with an assigned id")

        val foundOptional = chatRepository.findById(chat.id!!)
        assertTrue(foundOptional.isPresent, "We should be able to fetch the chat")

        val foundChat = foundOptional.get()
        assertEquals(chat.id, foundChat.id)
    }
}
