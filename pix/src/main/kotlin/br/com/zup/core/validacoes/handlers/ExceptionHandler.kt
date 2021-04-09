package br.com.zup.core.validacoes.handlers

import io.grpc.Metadata
import io.grpc.Status
import io.grpc.StatusRuntimeException

interface ExceptionHandler<E: java.lang.Exception> {

    fun handle(e: E): StatusWithDetails

    fun supports(e: Exception): Boolean

}

data class StatusWithDetails(val status: Status, val metadata: Metadata = Metadata()){
    constructor(se: StatusRuntimeException):
            this(se.status, se.trailers ?: Metadata())
    constructor(sp: com.google.rpc.Status):
            this(io.grpc.protobuf.StatusProto.toStatusRuntimeException(sp), )

    fun asRuntimeException(): StatusRuntimeException {
        return status.asRuntimeException(metadata)
    }
}