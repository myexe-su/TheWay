package su.myexe.app.config

import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.security.SecurityRequirement
import io.swagger.v3.oas.models.security.SecurityScheme
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class OpenApiConfig {

	@Bean
	fun openApi(): OpenAPI {
		val securitySchemeName = "bearerAuth"
		val securityScheme = SecurityScheme()
			.type(SecurityScheme.Type.HTTP)
			.scheme("bearer")
			.bearerFormat("JWT")

		return OpenAPI()
			.info(
				Info()
					.title("App API")
					.version("v1")
			)
			.components(
				Components()
					.addSecuritySchemes(securitySchemeName, securityScheme)
			)
			.addSecurityItem(SecurityRequirement().addList(securitySchemeName))
	}
}
