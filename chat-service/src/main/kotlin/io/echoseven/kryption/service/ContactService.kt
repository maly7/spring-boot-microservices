package io.echoseven.kryption.service

import io.echoseven.kryption.data.UserRepository
import io.echoseven.kryption.exception.BadRequestException
import io.echoseven.kryption.web.resource.Contact
import io.echoseven.kryption.web.resource.ContactRequest
import org.apache.commons.lang3.StringUtils
import org.springframework.stereotype.Service

@Service
class ContactService(
    val userRepository: UserRepository,
    val userService: UserService
) {

    fun getContacts(): List<Contact> {
        return userService.getCurrentUser().contacts.map { it.asContact() }
    }

    fun addContact(contactRequest: ContactRequest): List<Contact> {
        guardAgainstBadRequests(contactRequest)

        val contact = userService.findByEmail(contactRequest.email!!)
        val currentUser = userService.getCurrentUser()

        currentUser.contacts += contact
        userRepository.save(currentUser)

        return getContacts()
    }

    fun removeContact(contactRequest: ContactRequest) {
        guardAgainstBadRequests(contactRequest)

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
