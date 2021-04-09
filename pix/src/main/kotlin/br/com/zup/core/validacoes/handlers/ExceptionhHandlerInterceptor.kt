package br.com.zup.core.validacoes.handlers

import io.grpc.BindableService
import io.grpc.stub.StreamObserver
import io.micronaut.aop.MethodInterceptor
import io.micronaut.aop.MethodInvocationContext
import org.slf4j.LoggerFactory
import javax.inject.Singleton

@Singleton
class ExceptionHandlerInterceptor(
    private val resolver: ExceptionHandlerResolver
): MethodInterceptor<BindableService, Any?> {

    val logger = LoggerFactory.getLogger(this::class.java)

    override fun intercept(context: MethodInvocationContext<BindableService, Any?>): Any? {
        try {
            return context.proceed()
        }catch (e: Exception){
            logger.error("Handling the exception ${e.javaClass.name} while processing call: ${context?.targetMethod}", e)

            val handler = resolver.resolve(e)
            val status = handler?.handle(e)

            GrpcEndPointArguments(context).response()
                .onError(status?.asRuntimeException())

            return null
        }
    }
}

class GrpcEndPointArguments(val context: MethodInvocationContext<BindableService, Any?>) {
    fun response(): StreamObserver<*> {
        return context.parameterValues[1] as StreamObserver<*>
    }
}