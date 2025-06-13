import com.study.eventlab.common.exception.ApiException

sealed class ResponseResult<out T> {

    data class Success<out T>(val body: T) : ResponseResult<T>()
    data class Failure(val errorResponse: ErrorResponse) : ResponseResult<Nothing>()

    val isSuccess: Boolean
        get() = this is Success

    val isFailure: Boolean
        get() = this is Failure

    inline fun onSuccess(action: (T) -> Unit): ResponseResult<T> {
        if (this is Success) action(body)
        return this
    }

    inline fun onFailure(action: (ErrorResponse) -> Unit): ResponseResult<T> {
        if (this is Failure) action(errorResponse)
        return this
    }

    fun getOrNull(): T? = (this as? Success)?.body

    fun getOrThrow(): T = when (this) {
        is Success -> body
        is Failure -> throw ApiException(errorResponse, toErrorCode(errorResponse))
    }

    inline fun <R> getOrDefault(default: R, transform: (T) -> R): R =
        when (this) {
            is Success -> transform(body)
            is Failure -> default
        }

    private fun toErrorCode(error: ErrorResponse): ErrorCode =
        if (error.status in 400..499) ErrorCode.INVALID_INPUT_VALUE else ErrorCode.SERVER_ERROR
}

data class ErrorResponse(
    val message: String,
    val code: String,
    val status: Int
)

enum class ErrorCode {
    INVALID_INPUT_VALUE,
    SERVER_ERROR
}