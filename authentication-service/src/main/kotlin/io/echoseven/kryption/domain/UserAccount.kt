package io.echoseven.kryption.domain

import org.hibernate.annotations.GenericGenerator
import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.validation.constraints.NotBlank

@Entity
class UserAccount(@NotBlank @Column(name = "email", nullable = false) var email: String,
                  @NotBlank @Column(name = "password", nullable = false) var password: String,
                  @Column(name = "verified") var isVerified: Boolean) {
    @Id
    @Column(name = "id")
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    var id: String? = null

    @Column(name = "created_date", nullable = false)
    var createdDate: LocalDateTime? = null
}