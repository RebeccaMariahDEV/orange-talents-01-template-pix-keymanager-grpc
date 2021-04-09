package br.com.zup.chavePix.deletar

import br.com.zup.DeletarPixRequest
import br.com.zup.DeletarPixResponse
import br.com.zup.DeletarPixServiceGrpc
import br.com.zup.core.validacoes.handlers.ErrorHandler
import br.com.zup.grpc.cadastrar.paraDeletarChave
import io.grpc.stub.StreamObserver
import javax.inject.Singleton

@ErrorHandler
@Singleton
class DeletaChaveEndpoint (val deletarChave: DeletaPixService): DeletarPixServiceGrpc.DeletarPixServiceImplBase(){

    override fun deletar(request: DeletarPixRequest?,
                         responseObserver: StreamObserver<DeletarPixResponse>?){

        deletarChave.deletar(request?.paraDeletarChave()!!)
        val response = DeletarPixResponse.newBuilder()
        .setMessage("Chave removida com sucesso").build()
        responseObserver!!.onNext(response)
        responseObserver.onCompleted()

    }
}