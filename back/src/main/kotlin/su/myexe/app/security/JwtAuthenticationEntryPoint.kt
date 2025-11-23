package su.myexe.app.security

import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.stereotype.Component
import su.myexe.app.exception.ApiErrorResponse

@Component
class JwtAuthenticationEntryPoint(
	private val objectMapper: ObjectMapper
) : AuthenticationEntryPoint {
	override fun commence(
		request: HttpServletRequest,
		response: HttpServletResponse,
		authException: AuthenticationException
	) {
		val status = HttpStatus.UNAUTHORIZED
		val body = ApiErrorResponse(
			status = status.value(),
			error = status.reasonPhrase,
			message = "Authentication required",
			messageKey = "auth.unauthorized",
			params = emptyMap(),
			path = request.requestURI
		)
		response.status = status.value()
		response.contentType = MediaType.APPLICATION_JSON_VALUE
		response.writer.use { writer ->
			writer.write(objectMapper.writeValueAsString(body))
		}
	}
}
