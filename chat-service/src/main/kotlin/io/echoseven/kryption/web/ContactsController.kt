package io.echoseven.kryption.web

import io.echoseven.kryption.service.ContactService
import io.echoseven.kryption.web.resource.ContactRequest
import org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController
@RequestMapping("/user/contacts")
class ContactsController(val contactService: ContactService) {

    @GetMapping(produces = [APPLICATION_JSON_UTF8_VALUE])
    fun getContacts() = contactService.getContacts()

    @PostMapping(consumes = [APPLICATION_JSON_UTF8_VALUE], produces = [APPLICATION_JSON_UTF8_VALUE])
    fun addContact(@Valid @RequestBody contactRequest: ContactRequest) = contactService.addContact(contactRequest)

    // DELETE /contacts - delete a single contact
}
