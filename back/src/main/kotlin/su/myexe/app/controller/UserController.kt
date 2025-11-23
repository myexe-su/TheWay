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
import su.myexe.app.dto.UserMapper
import su.myexe.app.dto.UserRequest
import su.myexe.app.dto.UserResponse
import su.myexe.app.service.UserService

@RestController
@RequestMapping("/api/users")
class UserController(
	private val service: UserService
) {

	@GetMapping
	fun list(): List<UserResponse> =
		service.findAll().map(UserMapper::toResponse)

	@GetMapping("/{id}")
	fun get(@PathVariable id: Long): UserResponse =
		UserMapper.toResponse(service.findById(id))

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	fun create(@Valid @RequestBody request: UserRequest): UserResponse =
		UserMapper.toResponse(service.create(request))

	@PutMapping("/{id}")
	fun update(@PathVariable id: Long, @Valid @RequestBody request: UserRequest): UserResponse =
		UserMapper.toResponse(service.update(id, request))

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	fun delete(@PathVariable id: Long) {
		service.delete(id)
	}
}
