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
import su.myexe.app.dto.CompanyMapper
import su.myexe.app.dto.CompanyRequest
import su.myexe.app.dto.CompanyResponse
import su.myexe.app.service.CompanyService

@RestController
@RequestMapping("/api/companies")
class CompanyController(
	private val service: CompanyService
) {

	@GetMapping
	fun list(): List<CompanyResponse> =
		service.findAll().map(CompanyMapper::toResponse)

	@GetMapping("/{id}")
	fun get(@PathVariable id: Long): CompanyResponse =
		CompanyMapper.toResponse(service.findById(id))

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	fun create(@Valid @RequestBody request: CompanyRequest): CompanyResponse =
		CompanyMapper.toResponse(service.create(request))

	@PutMapping("/{id}")
	fun update(@PathVariable id: Long, @Valid @RequestBody request: CompanyRequest): CompanyResponse =
		CompanyMapper.toResponse(service.update(id, request))

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	fun delete(@PathVariable id: Long) {
		service.delete(id)
	}
}
