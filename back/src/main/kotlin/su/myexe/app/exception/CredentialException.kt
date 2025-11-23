package su.myexe.app.exception

abstract class CredentialException(
    val messageKey: String,
    val parameters: Map<String, String> = emptyMap(),
    override val message: String
) : RuntimeException(message)

class CredentialNotFoundException(id: Long) :
    CredentialException(
        messageKey = "credential.notFound",
        parameters = mapOf("id" to id.toString()),
        message = "Credential with id=$id not found"
    )

class MissingCredentialDataException(userId: Long) :
    CredentialException(
        messageKey = "credential.missingData",
        parameters = mapOf("userId" to userId.toString()),
        message = "Credential data incomplete for user id=$userId"
    )
