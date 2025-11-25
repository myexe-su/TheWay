package su.myexe.app.dto

import jakarta.validation.constraints.NotBlank
import su.myexe.app.model.Company

data class CompanyRequest(
	@field:NotBlank
	val name: String,
	val descr: String?
)

data class CompanyResponse(
	val id: Long,
	val name: String,
	val descr: String?
)

object CompanyMapper {
	fun toResponse(entity: Company) = CompanyResponse(
		id = entity.id ?: throw IllegalStateException("Company id must not be null"),
		name = entity.name,
		descr = entity.descr
	)
}
