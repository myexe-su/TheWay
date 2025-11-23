package su.myexe.app.config

import org.slf4j.LoggerFactory
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.stereotype.Component
import su.myexe.app.dto.CredentialRequest
import su.myexe.app.dto.UserRequest
import su.myexe.app.model.CredentialProvider
import su.myexe.app.repository.UserCredentialRepository
import su.myexe.app.service.UserService

@Component
class DefaultAdminInitializer(
	private val userService: UserService,
	private val credentialRepository: UserCredentialRepository
) : ApplicationRunner {

	private val logger = LoggerFactory.getLogger(DefaultAdminInitializer::class.java)

	override fun run(args: ApplicationArguments?) {
			if (!adminEnabled()) {
				return
			}

			val login = adminLogin()
			val existing = credentialRepository.findByProviderAndLogin(
				CredentialProvider.LOCAL,
				login
			)
			if (existing != null) {
				return
			}

			logger.info("Creating default admin user with login {}", login)
			val user = userService.create(UserRequest(name = adminName()))
			userService.addCredential(
				user.id!!,
				CredentialRequest(
					provider = CredentialProvider.LOCAL,
					login = login,
					password = adminPassword(),
					refreshToken = null
				)
			)
		}

    private fun adminEnabled(): Boolean =
        System.getProperty("app.security.default-admin.enabled")?.toBooleanStrictOrNull()
            ?: System.getenv("APP_SECURITY_DEFAULT_ADMIN_ENABLED")?.toBooleanStrictOrNull()
            ?: true

    private fun adminName(): String =
        System.getProperty("app.security.default-admin.name")
            ?: System.getenv("APP_SECURITY_DEFAULT_ADMIN_NAME")
            ?: "Root User"

    private fun adminLogin(): String =
        System.getProperty("app.security.default-admin.login")
            ?: System.getenv("APP_SECURITY_DEFAULT_ADMIN_LOGIN")
            ?: "root"

    private fun adminPassword(): String =
        System.getProperty("app.security.default-admin.password")
            ?: System.getenv("APP_SECURITY_DEFAULT_ADMIN_PASSWORD")
            ?: "password"
}
