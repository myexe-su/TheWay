package su.myexe.app.auth

import jakarta.validation.constraints.NotBlank

data class LoginRequest(
	@get:NotBlank
	val login: String,
	@get:NotBlank
	val password: String
)

data class TokenResponse(
	val token: String
)
