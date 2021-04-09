package br.com.zup.chavePix.cadastrar


import br.com.zup.PixRequest
import br.com.zup.PixResponse
import br.com.zup.cadastraChavePixGrpcGrpc
import br.com.zup.core.validacoes.handlers.ErrorHandler
import br.com.zup.grpc.cadastrar.paraChave
import io.grpc.stub.StreamObserver
import javax.inject.Singleton

@ErrorHandler
@Singleton
class CadastraChavePixEndpoint (val service: CadastraPixServece)
    : cadastraChavePixGrpcGrpc.cadastraChavePixGrpcImplBase() {


     override fun cadastrar(request: PixRequest?,
                            responseObserver: StreamObserver<PixResponse>?) {


        val novaChave = request?.paraChave()
        val chaveCriada = service.registrar(novaChave)

        val response = PixResponse.newBuilder()
            .setClietId(chaveCriada.id.toString()).build()

        responseObserver!!.onNext(response)
        responseObserver.onCompleted()
    }
}