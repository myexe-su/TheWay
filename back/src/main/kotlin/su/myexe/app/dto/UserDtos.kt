package su.myexe.app.dto

import jakarta.validation.constraints.NotBlank
import su.myexe.app.model.User

data class UserRequest(
	@field:NotBlank
	val name: String
)

data class UserResponse(
	val id: Long,
	val name: String
)

object UserMapper {
	fun toResponse(user: User) = UserResponse(
		id = user.id ?: throw IllegalStateException("User id must not be null"),
		name = user.name
	)
}
