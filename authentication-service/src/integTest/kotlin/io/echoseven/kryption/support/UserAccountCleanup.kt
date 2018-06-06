package io.echoseven.kryption.support

import io.echoseven.kryption.data.UserAccountRepository
import org.junit.After
import org.springframework.beans.factory.annotation.Autowired

open class UserAccountCleanup {

    @Autowired
    lateinit var userAccountRepository: UserAccountRepository

    @After
    fun cleanup() {
        userAccountRepository.deleteAll()
    }
}
