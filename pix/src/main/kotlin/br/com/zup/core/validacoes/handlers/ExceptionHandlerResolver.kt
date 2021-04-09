package br.com.zup.core.validacoes.handlers

import io.grpc.Status
import io.micronaut.http.client.exceptions.HttpClientResponseException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ExceptionHandlerResolver(
    @Inject private val handlers: List<ExceptionHandler<Exception>>
) {

    private var defaultHandler: ExceptionHandler<Exception> = DefaultExceptionHandler()

    constructor(handlers: List<ExceptionHandler<Exception>>,
                defaultHandler: ExceptionHandler<Exception>): this(handlers) {
        this.defaultHandler = defaultHandler
    }

    fun resolve(e: Exception): ExceptionHandler<Exception>? {
        val foundHandlers = handlers.filter { h -> h.supports(e) }

        if(foundHandlers.size > 1)
            throw IllegalStateException("Too many handlers supporting the same exception ${e.cause}")

        return foundHandlers.firstOrNull() ?: defaultHandler
    }

}

class DefaultExceptionHandler : ExceptionHandler<Exception> {
    override fun handle(e: Exception): StatusWithDetails {
        when(e){
            is IllegalArgumentException -> Status.INVALID_ARGUMENT.withDescription(e.message)
            is IllegalStateException -> Status.FAILED_PRECONDITION.withDescription(e.message)
            is HttpClientResponseException -> Status.INTERNAL.withDescription("Erro interno de conexão!")
            else -> Status.UNKNOWN
        }.let { status->
            return StatusWithDetails(status.withCause(e))
        }
    }

    override fun supports(e: Exception): Boolean {
        return e is Exception
    }

}
