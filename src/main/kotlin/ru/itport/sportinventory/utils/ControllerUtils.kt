package ru.itport.sportinventory.utils

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import ru.itport.sportinventory.exception.ErrorDescriptor
import ru.itport.sportinventory.exception.PlatformException
import ru.itport.sportinventory.models.BaseResponse

class ControllerUtils {
    companion object {
        val logger: Logger = LoggerFactory.getLogger(ControllerUtils::class.java)
        fun <T> serviceCall(function: () -> T): BaseResponse<T> {
            runCatching {
                return BaseResponse.ok(function() )
            }.getOrElse {
                logger.error("ERROR: ${it.message}")
                return when (it) {
                    is PlatformException -> BaseResponse.fail<T>(it.getError())
                    else -> BaseResponse.fail<T>(ErrorDescriptor.INTERNAL_ERROR)
                }
            }
        }
    }
}

