package su.myexe.app.exception

abstract class CompanyException(
    val messageKey: String,
    val parameters: Map<String, String> = emptyMap(),
    override val message: String
) : RuntimeException(message)

class CompanyNotFoundException(id: Long) :
    CompanyException(
        messageKey = "company.notFound",
        parameters = mapOf("id" to id.toString()),
        message = "Company with id=$id not found"
    )
