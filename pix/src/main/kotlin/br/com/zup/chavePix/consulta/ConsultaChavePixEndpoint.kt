package br.com.zup.chavePix.consulta

import br.com.zup.*
import br.com.zup.chavePix.ChavePixCliente
import br.com.zup.chavePix.ChavePixRepository
import br.com.zup.grpc.cadastrar.paraFiltro
import io.grpc.stub.StreamObserver
import javax.validation.Validator


class ConsultaChavePixEndpoint (val repository: ChavePixRepository,
val cliente: ChavePixCliente,
val validator: Validator):
ConsultaChavePixGrpcServiceGrpc.ConsultaChavePixGrpcServiceImplBase(){

 override fun consultar(request: ConsultaChavePixRequest?,
 responseObserver: StreamObserver<ConsultaChavePixResponse>?){
  val filtro = request?.paraFiltro(validator)
  val chavePix = filtro?.filtra(repository, cliente)

  val response = ConsultaChavePixResponse.newBuilder()
   .setTipoDaChave(chavePix?.tipo)
   .setChave(chavePix?.chaveCadastrada)
   .setTitular(
    TitularResponse.newBuilder()
    .setCpf(chavePix?.conta?.titular?.cpf)
    .setNome(chavePix?.conta?.titular?.nome)
   )
   .setConta(
    ContaResponse.newBuilder()
    .setNome(chavePix?.conta?.instituicao?.nome)
    .setAgencia(chavePix?.conta?.agencia)
    .setNumero(chavePix?.conta?.numero)
    .setTipoDaConta(chavePix?.conta?.tipo?.name)
   )
   .build()

  responseObserver!!.onNext(response)
  responseObserver.onCompleted()
 }

}