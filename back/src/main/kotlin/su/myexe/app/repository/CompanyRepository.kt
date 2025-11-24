package su.myexe.app.repository

import org.springframework.data.jpa.repository.JpaRepository
import su.myexe.app.model.Company

interface CompanyRepository : JpaRepository<Company, Long>
