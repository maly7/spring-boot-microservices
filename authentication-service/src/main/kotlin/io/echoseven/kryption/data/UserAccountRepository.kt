package io.echoseven.kryption.data

import io.echoseven.kryption.domain.UserAccount
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface UserAccountRepository : CrudRepository<UserAccount, String> {
    fun findByEmail(email: String): Optional<UserAccount>
}
