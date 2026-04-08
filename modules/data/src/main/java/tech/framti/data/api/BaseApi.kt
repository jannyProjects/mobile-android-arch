package tech.framti.data.api

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import retrofit2.Response
import tech.framti.domain.exception.ApiException
import timber.log.Timber

abstract class BaseApi {

    suspend inline fun <reified T : Any> handleApi(
        crossinline execute: suspend () -> Response<T>
    ): T? {
        return withContext(Dispatchers.IO) {
            return@withContext try {
                val response = execute()
                val body = response.body()
                if (response.isSuccessful) {
                    if (T::class == Unit::class) {
                        Timber.d("Api call success (no content)")
                        return@withContext Unit as T
                    }
                    Timber.d("Api call response: $body")
                    body
                } else {
                    val errorBody = response.errorBody()?.string() ?: ""
                    val responseCode = response.code()

                    Timber.e("Api call has error: $responseCode: $errorBody")

                    throw ApiException(
                        errorCode = responseCode,
                        errorMessage = errorBody
                    )
                }

            } catch (e: HttpException) {
                Timber.e("Api call has http error: ${e.message}}")
                throw e
            } catch (e: Throwable) {
                Timber.e("Api call throws exception: ${e.message}")
                throw e
            }
        }
    }
}

