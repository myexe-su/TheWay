package su.myexe.app.repository

import org.springframework.data.jpa.repository.JpaRepository
import su.myexe.app.model.CredentialProvider
import su.myexe.app.model.UserCredential

interface UserCredentialRepository : JpaRepository<UserCredential, Long> {
	fun findAllByUserId(userId: Long): List<UserCredential>
	fun findByProviderAndLogin(provider: CredentialProvider, login: String): UserCredential?
}
