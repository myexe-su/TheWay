package su.myexe.app.exception

abstract class UserException(
    val messageKey: String,
    val parameters: Map<String, String> = emptyMap(),
    override val message: String
) : RuntimeException(message)

class UserNotFoundException(id: Long) :
    UserException(
        messageKey = "user.notFound",
        parameters = mapOf("id" to id.toString()),
        message = "User with id=$id not found"
    )
