package com.github.maly7.support

import com.github.maly7.data.UserAccountRepository
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
