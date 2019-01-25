package com.github.maly7.domain

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime
import java.util.UUID
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.EntityListeners
import javax.persistence.Id
import javax.validation.constraints.NotBlank

@Entity
@EntityListeners(AuditingEntityListener::class)
data class UserAccount(
    @NotBlank @Column(name = "email", nullable = false) var email: String? = null,
    @NotBlank @Column(name = "password", nullable = false) var password: String? = null,
    @Column(name = "verified") var isVerified: Boolean = false
) {
    @Id
    @Column(name = "id")
    var id: String? = UUID.randomUUID().toString()

    @CreatedDate
    @Column(name = "created_date", nullable = false)
    var createdDate: LocalDateTime? = null
}
