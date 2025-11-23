package su.myexe.app.auth

import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import su.myexe.app.model.CredentialProvider
import su.myexe.app.repository.UserCredentialRepository
import su.myexe.app.security.JwtService
import su.myexe.app.exception.AbstractApiException
import org.springframework.http.HttpStatus

@Service
class AuthService(
	private val credentialRepository: UserCredentialRepository,
	private val passwordEncoder: PasswordEncoder,
	private val jwtService: JwtService
) {

	@Transactional(readOnly = true)
	fun login(request: LoginRequest): TokenResponse {
		val credential = credentialRepository.findByProviderAndLogin(CredentialProvider.LOCAL, request.login)
			?: throw InvalidCredentialsException(request.login)

		val storedPassword = credential.password
		if (storedPassword.isNullOrBlank() || !passwordEncoder.matches(request.password, storedPassword)) {
			throw InvalidCredentialsException(request.login)
		}

		val userId = credential.user.id
			?: throw InvalidCredentialsException(request.login)

		val token = jwtService.generateToken(userId, credential.login)
		return TokenResponse(token)
	}
}

class InvalidCredentialsException(login: String) :
	AbstractApiException(
		status = HttpStatus.UNAUTHORIZED,
		messageKey = "auth.invalidCredentials",
		parameters = mapOf("login" to login),
		debugMessage = "Invalid credentials for login '$login'"
	)
