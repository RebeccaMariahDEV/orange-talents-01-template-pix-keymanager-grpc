package br.com.zup.chavePix.listar

import br.com.zup.ChaveResponse
import br.com.zup.ListaChavePixRequest
import br.com.zup.ListaChavePixResponse
import br.com.zup.ListaChavePixServiceGrpc
import br.com.zup.grpc.cadastrar.paraListaChavePix
import io.grpc.stub.StreamObserver
import br.com.zup.grpc.cadastrar.*

class ListaChavePixEndpoint (val listaChaveService: ListaChaveService):
    ListaChavePixServiceGrpc.ListaChavePixServiceImplBase() {

        override fun listar(request: ListaChavePixRequest?, responseObserver: StreamObserver<ListaChavePixResponse>?){

            val dadosParaConsulta = request?.paraListaChavePix()
            val chaves = listaChaveService.listar(dadosParaConsulta)

            var chavesResponse: MutableList<ChaveResponse> = mutableListOf()

            chaves.forEach {
                chavesResponse.add(ChaveResponse.newBuilder()
                    .setIdChave(it?.id!!)
                    .setIdCliente(it?.conta.titular?.id)
                    .setChave(it.chaveCadastrada)
                    .setTipoConta(it.conta.tipo?.name)
//                    .setCriadoEm(it.criadaEm?.toTimestampGrpc())
                    .build())
            }

            var response = ListaChavePixResponse.newBuilder()
                .addAllChaves(chavesResponse)
                .build()

            responseObserver!!.onNext(response)
            responseObserver.onCompleted()
        }
}