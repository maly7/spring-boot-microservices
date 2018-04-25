package io.echoseven.kryption.service

import io.echoseven.kryption.data.UserRepository
import io.echoseven.kryption.exception.BadRequestException
import io.echoseven.kryption.web.resource.Contact
import io.echoseven.kryption.web.resource.ContactRequest
import org.apache.commons.lang3.StringUtils
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class ContactService(
    val userRepository: UserRepository,
    val userService: UserService
) {
    private val log = LoggerFactory.getLogger(ContactService::class.java)

    fun getContacts(): List<Contact> {
        return userService.getCurrentUser().contacts.map { it.asContact() }
    }

    fun addContact(contactRequest: ContactRequest): List<Contact> {
        guardAgainstBadRequests(contactRequest)
        log.debug("Attempting to add user [{}] to contact list", contactRequest.email)

        val contact = userService.findByEmail(contactRequest.email!!)
        val currentUser = userService.getCurrentUser()

        currentUser.contacts += contact
        userRepository.save(currentUser)

        return getContacts()
    }

    fun removeContact(contactRequest: ContactRequest) {
        guardAgainstBadRequests(contactRequest)
        log.debug("Attempting to remove user [{}] from contact list", contactRequest.email)

        val contact = userService.findByEmail(contactRequest.email!!)
        val currentUser = userService.getCurrentUser()

        currentUser.contacts -= contact
        userRepository.save(currentUser)
    }

    private fun guardAgainstBadRequests(contactRequest: ContactRequest) {
        if (StringUtils.isBlank(contactRequest.email)) {
            throw BadRequestException("A Contact request must have an email")
        }
    }
}
