package tech.framti.domain.exception

open class ActivationException(
    cause: Throwable? = null,
    open val errorMessage: String? = null
) : BaseException(cause) {
    override val message: String?
        get() = errorMessage ?: super.message
}