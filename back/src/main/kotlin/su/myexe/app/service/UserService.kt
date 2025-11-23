package su.myexe.app.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.security.crypto.password.PasswordEncoder
import su.myexe.app.dto.CredentialRequest
import su.myexe.app.dto.UserRequest
import su.myexe.app.model.CredentialProvider
import su.myexe.app.model.User
import su.myexe.app.model.UserCredential
import su.myexe.app.repository.UserCredentialRepository
import su.myexe.app.repository.UserRepository
import su.myexe.app.exception.AbstractApiException
import org.springframework.http.HttpStatus

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
	fun listCredentials(userId: Long): List<UserCredential> {
		findById(userId)
		return credentialRepository.findAllByUserId(userId)
	}

	@Transactional
	fun addCredential(userId: Long, request: CredentialRequest): UserCredential {
		val user = findById(userId)
		if (request.password.isNullOrBlank() && request.provider == CredentialProvider.LOCAL) {
			throw MissingCredentialDataException(userId)
		}
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

class UserNotFoundException(id: Long) :
	AbstractApiException(
		status = HttpStatus.NOT_FOUND,
		messageKey = "user.notFound",
		parameters = mapOf("id" to id.toString()),
		debugMessage = "User with id=$id not found"
	)

class CredentialNotFoundException(id: Long) :
	AbstractApiException(
		status = HttpStatus.NOT_FOUND,
		messageKey = "credential.notFound",
		parameters = mapOf("id" to id.toString()),
		debugMessage = "Credential with id=$id not found"
	)

class MissingCredentialDataException(userId: Long) :
	AbstractApiException(
		status = HttpStatus.BAD_REQUEST,
		messageKey = "credential.missingData",
		parameters = mapOf("userId" to userId.toString()),
		debugMessage = "Credential data incomplete for user id=$userId"
	)
