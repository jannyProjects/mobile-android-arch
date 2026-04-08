package tech.framti.domain.exception

abstract class BaseException(
    cause: Throwable? = null
) : Exception(cause)
