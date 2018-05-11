package io.echoseven.kryption.data

import io.echoseven.kryption.ChatIntegrationTest
import io.echoseven.kryption.domain.Chat
import io.echoseven.kryption.domain.ChatMessage
import org.hamcrest.Matchers.hasItem
import org.hamcrest.Matchers.hasSize
import org.junit.After
import org.junit.Assert.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.junit4.SpringRunner
import java.time.ZonedDateTime
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

@RunWith(SpringRunner::class)
@ChatIntegrationTest
class ChatRepositoryTests {

    @Autowired
    lateinit var chatRepository: ChatRepository

    @Autowired
    lateinit var chatMessageRepository: ChatMessageRepository

    @After
    fun cleanup() {
        chatMessageRepository.deleteAll()
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

    @Test
    fun `add messages to a chat`() {
        val chat = chatRepository.save(Chat())

        val messages = listOf(
            chatMessageRepository.save(ChatMessage("test message", "foo", "bar", ZonedDateTime.now())),
            chatMessageRepository.save(ChatMessage("second message", "bar", "foo", ZonedDateTime.now()))
        )
        chat.messages = messages

        val updatedChat = chatRepository.save(chat)

        assertThat("There should be two messages", updatedChat.messages, hasSize(2))
        messages.forEach { assertThat("The message should be in the chat", chat.messages, hasItem(it)) }
    }
}
