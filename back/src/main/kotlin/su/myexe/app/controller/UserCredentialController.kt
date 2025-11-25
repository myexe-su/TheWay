package su.myexe.app.controller

import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import su.myexe.app.dto.CredentialMapper
import su.myexe.app.dto.CredentialRequest
import su.myexe.app.dto.CredentialResponse
import su.myexe.app.service.UserService

@RestController
@RequestMapping("/api/users/{userId}/credentials")
class UserCredentialController(
	private val service: UserService
) {

	@GetMapping
	fun list(
		@PathVariable userId: Long
	): List<CredentialResponse> = service.listCredentials(userId).map(CredentialMapper::toResponse)

	@GetMapping("/{credentialId}")
	fun get(
		@PathVariable credentialId: Long
	): CredentialResponse = CredentialMapper.toResponse(service.getCredential(credentialId))

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	fun create(
		@PathVariable userId: Long,
		@Valid @RequestBody request: CredentialRequest
	): CredentialResponse = CredentialMapper.toResponse(service.addCredential(userId, request))

	@DeleteMapping("/{credentialId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	fun delete(
		@PathVariable credentialId: Long
	) {
		service.deleteCredential(credentialId)
	}
}
