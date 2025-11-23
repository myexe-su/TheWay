package su.myexe.app.service

import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.security.crypto.password.PasswordEncoder
import su.myexe.app.dto.CredentialRequest
import su.myexe.app.dto.UserRequest
import su.myexe.app.model.CredentialProvider
import su.myexe.app.model.User
import su.myexe.app.model.UserCredential
import su.myexe.app.repository.UserCredentialRepository
import su.myexe.app.repository.UserRepository

@Service
class UserService(
	private val repository: UserRepository,
	private val credentialRepository: UserCredentialRepository,
	private val passwordEncoder: PasswordEncoder
) {

	@Transactional(readOnly = true)
	fun findAll(): List<User> = repository.findAll()

	@Transactional(readOnly = true)
	fun findById(id: Long): User =
		repository.findById(id).orElseThrow { UserNotFoundException(id) }

	@Transactional
	fun create(request: UserRequest): User {
		val user = User(name = request.name)
		return repository.save(user)
	}

	@Transactional
	fun update(id: Long, request: UserRequest): User {
		val user = findById(id)
		user.name = request.name
		return repository.save(user)
	}

	@Transactional
	fun delete(id: Long) {
		if (!repository.existsById(id)) {
			throw UserNotFoundException(id)
		}
		repository.deleteById(id)
	}

	@Transactional(readOnly = true)
	fun getCredential(id: Long): UserCredential =
		credentialRepository.findById(id).orElseThrow { CredentialNotFoundException(id) }

	@Transactional(readOnly = true)
	fun listCredentials(userId: Long): List<UserCredential> =
		credentialRepository.findAllByUserId(userId)

	@Transactional
	fun addCredential(userId: Long, request: CredentialRequest): UserCredential {
		val user = findById(userId)
		val encodedPassword = encodePasswordIfNeeded(request.provider, request.password)
		val credential = UserCredential(
			provider = request.provider,
			login = request.login,
			password = encodedPassword,
			refreshToken = request.refreshToken,
			user = user
		)
		user.credentials.add(credential)
		return credentialRepository.save(credential)
	}

	@Transactional
	fun updateCredential(id: Long, request: CredentialRequest): UserCredential {
		val credential = getCredential(id)
		val encodedPassword = encodePasswordIfNeeded(
			request.provider,
			request.password,
			current = credential.password
		)
		val updated = UserCredential(
			id = credential.id,
			provider = request.provider,
			login = request.login,
			password = encodedPassword,
			refreshToken = request.refreshToken ?: credential.refreshToken,
			user = credential.user
		)
		return credentialRepository.save(updated)
	}

	@Transactional
	fun deleteCredential(id: Long) {
		if (!credentialRepository.existsById(id)) {
			throw CredentialNotFoundException(id)
		}
		credentialRepository.deleteById(id)
	}

	private fun encodePasswordIfNeeded(
		provider: CredentialProvider,
		password: String?,
		current: String? = null
	): String? {
		if (provider != CredentialProvider.LOCAL) {
			return password ?: current
		}
		return password?.let { passwordEncoder.encode(it) } ?: current
	}
}

@ResponseStatus(HttpStatus.NOT_FOUND)
class UserNotFoundException(id: Long) :
	RuntimeException("User with id=$id not found")

@ResponseStatus(HttpStatus.NOT_FOUND)
class CredentialNotFoundException(id: Long) :
	RuntimeException("Credential with id=$id not found")
