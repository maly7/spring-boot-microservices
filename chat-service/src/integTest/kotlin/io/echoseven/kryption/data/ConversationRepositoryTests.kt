package io.echoseven.kryption.data

import io.echoseven.kryption.ChatIntegrationTest
import io.echoseven.kryption.domain.Conversation
import io.echoseven.kryption.domain.ConversationMessage
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
class ConversationRepositoryTests {

    @Autowired
    lateinit var conversationRepository: ConversationRepository

    @Autowired
    lateinit var conversationMessageRepository: ConversationMessageRepository

    @Autowired
    lateinit var userRepository: UserRepository

    @After
    fun cleanup() {
        conversationMessageRepository.deleteAll()
        conversationRepository.deleteAll()
        userRepository.deleteAll()
    }

    @Test
    fun `create a new chat`() {
        val chat = conversationRepository.insert(Conversation())

        assertNotNull(chat.id, "The chat should be saved with an assigned id")

        val foundOptional = conversationRepository.findById(chat.id!!)
        assertTrue(foundOptional.isPresent, "We should be able to fetch the chat")

        val foundChat = foundOptional.get()
        assertEquals(chat.id, foundChat.id)
    }

    @Test
    fun `add messages to a chat`() {
        val chat = conversationRepository.insert(Conversation())

        val messages = listOf(
            conversationMessageRepository.insert(
                ConversationMessage(
                    "test message",
                    "foo",
                    "bar",
                    Date.from(Instant.now())
                )
            ),
            conversationMessageRepository.insert(
                ConversationMessage(
                    "second message",
                    "bar",
                    "foo",
                    Date.from(Instant.now())
                )
            )
        )
        chat.messages = messages

        val updatedChat = conversationRepository.save(chat)

        assertThat("There should be two messages", updatedChat.messages, hasSize(2))
        messages.forEach { assertThat("The message should be in the chat", chat.messages, hasItem(it)) }
    }

    @Test
    fun `add chats to users`() {
        val userOne = userRepository.insert(User("email@foo.com"))
        val userTwo = userRepository.insert(User("foo@bar.com"))

        val userOneId: String = userOne.id!!
        val userTwoId: String = userTwo.id!!

        val firstMessage =
            conversationMessageRepository.insert(
                ConversationMessage(
                    "hello from user one",
                    userOneId,
                    userTwoId,
                    Date.from(Instant.now())
                )
            )

        val chat = conversationRepository.insert(Conversation(listOf(firstMessage), listOf(userOneId, userTwoId)))

        userOne.conversations = listOf(chat)
        userTwo.conversations = listOf(chat)

        userRepository.saveAll(listOf(userOne, userTwo))

        val fetchUserOne = userRepository.findById(userOneId).orElseThrow { IllegalStateException() }
        val fetchUserTwo = userRepository.findById(userTwoId).orElseThrow { IllegalStateException() }

        assertThat("The first user should have the chat", fetchUserOne.conversations, hasItem(chat))
        assertThat("The second user should have the chat", fetchUserTwo.conversations, hasItem(chat))

        assertEquals(
            fetchUserOne.conversations,
            fetchUserTwo.conversations,
            "The user's conversations should be the same"
        )
    }
}
