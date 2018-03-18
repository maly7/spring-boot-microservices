package io.echoseven.kryption.data

import io.echoseven.kryption.domain.UserAccount
import org.springframework.data.repository.CrudRepository

interface UserAccountRepository : CrudRepository<UserAccount, String>
