package io.echoseven.kryption.tokens

import io.echoseven.kryption.config.TokenSettings
import io.echoseven.kryption.domain.UserAccount
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.stereotype.Component

@Component
class TokenIssuer(private val tokenSettings: TokenSettings) {
    fun issueToken(userAccount: UserAccount): String {
        return Jwts.builder()
                .setSubject(userAccount.email)
                .setId(userAccount.id)
                .signWith(SignatureAlgorithm.RS512, tokenSettings.secret)
                .compact()
    }
}
