package io.echoseven.kryption.tokens

import io.echoseven.kryption.config.TokenSettings
import io.echoseven.kryption.domain.UserAccount
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.stereotype.Component

@Component
class TokenIssuer(private val tokenSettings: TokenSettings) {
    fun issueToken(userAccount: UserAccount): String {
        return Jwts.builder()
                .setSubject(userAccount.email)
                .setId(userAccount.id)
                .signWith(SignatureAlgorithm.HS512, tokenSettings.secret)
                .compact()
    }

    fun getEmailFromToken(token: String): String {
        return parseClaims(token).subject
    }

    private fun parseClaims(token: String): Claims {
        return Jwts.parser()
                .setSigningKey(tokenSettings.secret)
                .parseClaimsJws(token)
                .body
    }
}
