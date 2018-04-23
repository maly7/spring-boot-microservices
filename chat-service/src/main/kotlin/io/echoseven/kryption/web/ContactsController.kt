package io.echoseven.kryption.web

import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/user")
class ContactsController {
    // GET /contacts - return list of current users contacts
    // PUT /contacts - update the entire list of contacts
    // PUT /contact - add a single contact
    // DELETE /contact - delete a single contact
}
