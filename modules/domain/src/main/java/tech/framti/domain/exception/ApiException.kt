package tech.framti.domain.exception

open class ApiException(
    cause: Throwable? = null,
    open val errorCode: Int? = null,
    open val errorMessage: String? = null
) : BaseException(cause) {
    override val message: String?
        get() = errorMessage ?: super.message
}