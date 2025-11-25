package su.myexe.app.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import su.myexe.app.model.CredentialProvider
import su.myexe.app.model.UserCredential

data class CredentialRequest(
	@get:NotNull
	val provider: CredentialProvider,
	@get:NotBlank
	val login: String,
	val password: String? = null,
	val refreshToken: String? = null
)

data class CredentialResponse(
	val id: Long,
	val provider: CredentialProvider,
	val login: String,
	val refreshToken: String?
)

object CredentialMapper {
	fun toResponse(entity: UserCredential) = CredentialResponse(
		id = entity.id ?: throw IllegalStateException("Credential id must not be null"),
		provider = entity.provider,
		login = entity.login,
		refreshToken = entity.refreshToken
	)
}
