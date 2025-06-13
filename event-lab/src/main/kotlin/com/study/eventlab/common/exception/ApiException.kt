package com.study.eventlab.common.exception

import ErrorCode
import ErrorResponse

class ApiException(
    val errorResponse: ErrorResponse,
    val code: ErrorCode
) : RuntimeException(errorResponse.message)