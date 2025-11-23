package su.myexe.app.auth

import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import su.myexe.app.model.CredentialProvider
import su.myexe.app.repository.UserCredentialRepository
import su.myexe.app.security.JwtService
import su.myexe.app.exception.LoginFailedException

@Service
class AuthService(
	private val credentialRepository: UserCredentialRepository,
	private val passwordEncoder: PasswordEncoder,
	private val jwtService: JwtService
) {

	@Transactional(readOnly = true)
	fun login(request: LoginRequest): TokenResponse {
		val credential = credentialRepository.findByProviderAndLogin(CredentialProvider.LOCAL, request.login)
			?: throw LoginFailedException(request.login)

		val storedPassword = credential.password
		if (storedPassword.isNullOrBlank() || !passwordEncoder.matches(request.password, storedPassword)) {
			throw LoginFailedException(request.login)
		}

		val userId = credential.user.id
			?: throw LoginFailedException(request.login)

		val token = jwtService.generateToken(userId, credential.login)
		return TokenResponse(token)
	}
}
