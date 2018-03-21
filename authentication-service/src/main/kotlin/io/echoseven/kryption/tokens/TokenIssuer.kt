package io.echoseven.kryption.tokens

import io.echoseven.kryption.config.TokenSettings
import io.echoseven.kryption.domain.UserAccount
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.apache.commons.io.IOUtils
import org.springframework.core.io.ClassPathResource
import org.springframework.stereotype.Component
import java.security.Key
import java.security.KeyFactory
import java.security.spec.PKCS8EncodedKeySpec
import javax.annotation.PostConstruct

@Component
class TokenIssuer(private val tokenSettings: TokenSettings) {
    private val signatureAlgorithm = SignatureAlgorithm.RS256
    lateinit var signingKey: Key

    @PostConstruct
    fun init() {
        val bytes = IOUtils.toByteArray(ClassPathResource("keys/private_key.der").inputStream)
        val keySpec = PKCS8EncodedKeySpec(bytes)
        signingKey = KeyFactory.getInstance(signatureAlgorithm.familyName).generatePrivate(keySpec)
    }

    fun issueToken(userAccount: UserAccount): String {
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
