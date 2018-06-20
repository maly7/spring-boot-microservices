package io.echoseven.kryption.web.resource

// TODO: remove this class once https://github.com/spring-cloud/spring-cloud-gateway/issues/374 is resolved
data class DeleteResponse(val id: String, val entity: String, val status: String = "deleted")
