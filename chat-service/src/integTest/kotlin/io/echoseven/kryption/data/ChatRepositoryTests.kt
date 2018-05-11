package io.echoseven.kryption.data

import io.echoseven.kryption.ChatIntegrationTest
import io.echoseven.kryption.domain.Chat
import io.echoseven.kryption.domain.ChatMessage
import io.echoseven.kryption.domain.User
import org.hamcrest.Matchers.hasItem
import org.hamcrest.Matchers.hasSize
import org.junit.After
import org.junit.Assert.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.junit4.SpringRunner
import java.time.Instant
import java.util.Date
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

    @Autowired
    lateinit var userRepository: UserRepository

    @After
    fun cleanup() {
        chatMessageRepository.deleteAll()
        chatRepository.deleteAll()
        userRepository.deleteAll()
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
            chatMessageRepository.save(ChatMessage("test message", "foo", "bar", Date.from(Instant.now()))),
            chatMessageRepository.save(ChatMessage("second message", "bar", "foo", Date.from(Instant.now())))
        )
        chat.messages = messages

        val updatedChat = chatRepository.save(chat)

        assertThat("There should be two messages", updatedChat.messages, hasSize(2))
        messages.forEach { assertThat("The message should be in the chat", chat.messages, hasItem(it)) }
    }

    @Test
    fun `add chats to users`() {
        val userOne = userRepository.save(User("email@foo.com"))
        val userTwo = userRepository.save(User("foo@bar.com"))

        val userOneId: String = userOne.id!!
        val userTwoId: String = userTwo.id!!

        val firstMessage =
            chatMessageRepository.save(ChatMessage("hello from user one", userOneId, userTwoId, Date.from(Instant.now())))

        val chat = chatRepository.save(Chat(listOf(firstMessage)))

        userOne.chats = listOf(chat)
        userTwo.chats = listOf(chat)

        userRepository.saveAll(listOf(userOne, userTwo))

        val fetchUserOne = userRepository.findById(userOneId).orElseThrow { IllegalStateException() }
        val fetchUserTwo = userRepository.findById(userTwoId).orElseThrow { IllegalStateException() }

        assertThat("The first user should have the chat", fetchUserOne.chats, hasItem(chat))
        assertThat("The second user should have the chat", fetchUserTwo.chats, hasItem(chat))

        assertEquals(fetchUserOne.chats, fetchUserTwo.chats, "The user's chats should be the same")
    }
}
