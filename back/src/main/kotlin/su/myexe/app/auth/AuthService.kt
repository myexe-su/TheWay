package su.myexe.app.auth

import org.springframework.http.HttpStatus
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.ResponseStatus
import su.myexe.app.model.CredentialProvider
import su.myexe.app.repository.UserCredentialRepository
import su.myexe.app.security.JwtService

@Service
class AuthService(
	private val credentialRepository: UserCredentialRepository,
	private val passwordEncoder: PasswordEncoder,
	private val jwtService: JwtService
) {

	@Transactional(readOnly = true)
	fun login(request: LoginRequest): TokenResponse {
		val credential = credentialRepository.findByProviderAndLogin(CredentialProvider.LOCAL, request.login)
			?: throw InvalidCredentialsException()

		val storedPassword = credential.password
		if (storedPassword.isNullOrBlank() || !passwordEncoder.matches(request.password, storedPassword)) {
			throw InvalidCredentialsException()
		}

		val userId = credential.user.id
			?: throw InvalidCredentialsException()

		val token = jwtService.generateToken(userId, credential.login)
		return TokenResponse(token)
	}
}

@ResponseStatus(HttpStatus.UNAUTHORIZED)
class InvalidCredentialsException :
	RuntimeException("Invalid credentials")
