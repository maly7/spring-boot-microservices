package io.echoseven.kryption.web

import io.echoseven.kryption.service.ContactService
import io.echoseven.kryption.web.resource.ContactRequest
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController
@RequestMapping("/contacts")
class ContactsController(val contactService: ContactService) {

    @GetMapping(produces = [APPLICATION_JSON_UTF8_VALUE])
    fun getContacts() = contactService.getContacts()

    @PostMapping(consumes = [APPLICATION_JSON_UTF8_VALUE], produces = [APPLICATION_JSON_UTF8_VALUE])
    fun addContact(@Valid @RequestBody contactRequest: ContactRequest) = contactService.addContact(contactRequest)

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(consumes = [APPLICATION_JSON_UTF8_VALUE])
    fun removeContact(@Valid @RequestBody contactRequest: ContactRequest) = contactService.removeContact(contactRequest)
}
