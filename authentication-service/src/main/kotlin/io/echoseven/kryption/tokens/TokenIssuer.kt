package io.echoseven.kryption.tokens

import io.echoseven.kryption.domain.UserAccount
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.security.Key

@Component
class TokenIssuer(private val signingKey: Key, private val signatureAlgorithm: SignatureAlgorithm) {
    private val log = LoggerFactory.getLogger(TokenIssuer::class.java)

    fun issueToken(userAccount: UserAccount): String {
        log.debug("Issuing token for authenticated user [{}]", userAccount)

        return Jwts.builder()
            .setSubject(userAccount.email)
            .setId(userAccount.id)
            .signWith(signatureAlgorithm, signingKey)
            .compact()
    }

    fun getEmailFromToken(token: String): String = parseClaims(token).subject

    fun getIdFromToken(token: String): String = parseClaims(token).id

    private fun parseClaims(token: String): Claims {
        return Jwts.parser()
            .setSigningKey(signingKey)
            .parseClaimsJws(token)
            .body
    }
}
