package su.myexe.app.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import su.myexe.app.dto.CompanyRequest
import su.myexe.app.exception.CompanyNotFoundException
import su.myexe.app.model.Company
import su.myexe.app.repository.CompanyRepository

@Service
class CompanyService(
	private val repository: CompanyRepository
) {

	@Transactional(readOnly = true)
	fun findAll(): List<Company> = repository.findAll()

	@Transactional(readOnly = true)
	fun findById(id: Long): Company =
		repository.findById(id).orElseThrow { CompanyNotFoundException(id) }

	@Transactional
	fun create(request: CompanyRequest): Company {
		val company = Company(name = request.name, descr = request.descr)
		return repository.save(company)
	}

	@Transactional
	fun update(id: Long, request: CompanyRequest): Company {
		val company = findById(id)
		company.name = request.name
		company.descr = request.descr
		return repository.save(company)
	}

	@Transactional
	fun delete(id: Long) {
		if (!repository.existsById(id)) {
			throw CompanyNotFoundException(id)
		}
		repository.deleteById(id)
	}
}
