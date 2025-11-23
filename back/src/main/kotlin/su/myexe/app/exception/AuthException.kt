package su.myexe.app.exception

abstract class AuthException(
    val messageKey: String,
    val parameters: Map<String, String> = emptyMap(),
    override val message: String
) : RuntimeException(message)

class LoginFailedException(login: String) :
    AuthException(
        messageKey = "auth.loginFailed",
        parameters = mapOf("login" to login),
        message = "Login failed for '$login'"
    )
