package ru.pasha.network.api

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

@Serializable
data class ResponseWithStatus<out S : Any>(
    @SerialName("status") val status: ResponseStatusV1,
    @SerialName("success_data") val successData: S? = null,
    @SerialName("fail_data") val failData: ResponseFailDataV1? = null
) {
    fun toResult(): Result<S> {
        return when (status) {
            ResponseStatusV1.SUCCESS -> successData?.let { Result.success(it) }
                ?: Result.failure(MissingSuccessDataException())

            ResponseStatusV1.FAIL -> failData?.let { data ->
                Result.failure(
                    ApiFailureException(message = data.title + data.subtitle?.let { "\n" + it })
                )
            }
                ?: Result.failure(MissingFailDataException())
        }
    }
}

class MissingSuccessDataException : IllegalStateException("Success data is missing for SUCCESS status")
class MissingFailDataException : IllegalStateException("Fail data is missing for FAIL status")
class ApiFailureException(override val message: String) : IOException("API request failed")
class NullResponseBodyException : IOException("Response body is null")

@Suppress("TooGenericExceptionCaught")
inline fun <reified S : Any> handleApiResponse(
    block: () -> Response<ResponseWithStatus<S>>
): Result<S> {
    return try {
        val response = block()
        if (response.isSuccessful) {
            response.body()?.toResult() ?: Result.failure(NullResponseBodyException())
        } else {
            Result.failure(HttpException(response))
        }
    } catch (e: IOException) {
        Result.failure(e)
    } catch (e: HttpException) {
        Result.failure(e)
    } catch (e: Exception) {
        Result.failure(e)
    }
}

@Serializable
enum class ResponseStatusV1 {
    @SerialName("SUCCESS")
    SUCCESS,

    @SerialName("FAIL")
    FAIL
}

@Serializable
data class ResponseFailDataV1(
    @SerialName("title")
    val title: String,

    @SerialName("subtitle")
    val subtitle: String? = null,

    @SerialName("deeplink")
    val deeplink: String? = null
)
