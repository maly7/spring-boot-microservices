package io.echoseven.kryption.data

import io.echoseven.kryption.domain.UserAccount
import org.springframework.data.repository.CrudRepository
import java.util.*

interface UserAccountRepository : CrudRepository<UserAccount, String> {
    fun findByEmail(email: String): Optional<UserAccount>
}
