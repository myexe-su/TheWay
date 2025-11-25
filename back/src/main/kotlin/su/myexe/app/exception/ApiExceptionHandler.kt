package su.myexe.app.exception

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.FieldError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.context.request.WebRequest
import su.myexe.app.exception.CredentialException
import su.myexe.app.exception.CredentialNotFoundException
import su.myexe.app.exception.MissingCredentialDataException
import su.myexe.app.exception.UserException
import su.myexe.app.exception.UserNotFoundException
import su.myexe.app.exception.CompanyException
import su.myexe.app.exception.CompanyNotFoundException
import java.time.Instant

data class ApiErrorResponse(
	val timestamp: Instant = Instant.now(),
	val status: Int,
	val error: String,
	val message: String,
	val messageKey: String,
	val params: Map<String, String>,
	val path: String?
)

@RestControllerAdvice
class ApiExceptionHandler {

	@ExceptionHandler(
		UserException::class,
		CredentialException::class,
		CompanyException::class,
		AuthException::class
	)
	fun handleDomainExceptions(ex: RuntimeException, request: WebRequest): ResponseEntity<ApiErrorResponse> {
			val status = statusFor(ex)
			val response = ApiErrorResponse(
				status = status.value(),
				error = status.reasonPhrase,
				message = ex.message ?: status.reasonPhrase,
				messageKey = messageKey(ex),
				params = parameters(ex),
				path = request.getDescription(false).removePrefix("uri=")
			)
			return ResponseEntity.status(status).body(response)
		}

	@ExceptionHandler(MethodArgumentNotValidException::class)
	fun handleValidation(ex: MethodArgumentNotValidException, request: WebRequest): ResponseEntity<ApiErrorResponse> {
		val params = ex.bindingResult.fieldErrors.associate { error: FieldError ->
			error.field to (error.defaultMessage ?: "invalid")
		}
		val response = ApiErrorResponse(
			status = HttpStatus.BAD_REQUEST.value(),
			error = HttpStatus.BAD_REQUEST.reasonPhrase,
			message = "Validation failed",
			messageKey = "validation.failed",
			params = params,
			path = request.getDescription(false).removePrefix("uri=")
		)
		return ResponseEntity.badRequest().body(response)
	}

	@ExceptionHandler(Exception::class)
	fun handleOther(ex: Exception, request: WebRequest): ResponseEntity<ApiErrorResponse> {
		val response = ApiErrorResponse(
			status = HttpStatus.INTERNAL_SERVER_ERROR.value(),
			error = HttpStatus.INTERNAL_SERVER_ERROR.reasonPhrase,
			message = ex.message ?: "Unexpected error",
			messageKey = "error.unexpected",
			params = emptyMap(),
			path = request.getDescription(false).removePrefix("uri=")
		)
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response)
	}

	private fun statusFor(ex: RuntimeException): HttpStatus = when (ex) {
		is UserNotFoundException -> HttpStatus.NOT_FOUND
		is CredentialNotFoundException -> HttpStatus.NOT_FOUND
		is CompanyNotFoundException -> HttpStatus.NOT_FOUND
		is MissingCredentialDataException -> HttpStatus.BAD_REQUEST
		is AuthException -> HttpStatus.UNAUTHORIZED
		else -> HttpStatus.BAD_REQUEST
	}

	private fun messageKey(ex: RuntimeException): String = when (ex) {
		is UserException -> ex.messageKey
		is CredentialException -> ex.messageKey
		is CompanyException -> ex.messageKey
		is AuthException -> ex.messageKey
		else -> "error.unknown"
	}

	private fun parameters(ex: RuntimeException): Map<String, String> = when (ex) {
		is UserException -> ex.parameters
		is CredentialException -> ex.parameters
		is CompanyException -> ex.parameters
		is AuthException -> ex.parameters
		else -> emptyMap()
	}
}
