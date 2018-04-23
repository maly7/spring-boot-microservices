package io.echoseven.kryption.service

import io.echoseven.kryption.data.UserRepository
import io.echoseven.kryption.exception.BadRequestException
import io.echoseven.kryption.exception.NotFoundException
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
        return userService.getCurrentUser().contacts.map { Contact(it.email, it.onlineStatus, it.profileImageUrl) }
    }

    fun addContact(contactRequest: ContactRequest): List<Contact> {
        if (StringUtils.isBlank(contactRequest.email)) {
            throw BadRequestException("A Contact request must have an email")
        }

        val contact = userRepository.findByEmail(contactRequest.email!!).orElseThrow { NotFoundException("No user found with email ${contactRequest.email}") }
        val currentUser = userService.getCurrentUser()

        currentUser.contacts += contact
        userRepository.save(currentUser)

        return getContacts()
    }
}
