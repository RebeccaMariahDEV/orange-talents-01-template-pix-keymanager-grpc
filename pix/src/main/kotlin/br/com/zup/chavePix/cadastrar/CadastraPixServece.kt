package br.com.zup.grpc.cadastrar

import br.com.zup.DesafioPix
import br.com.zup.chavePix.ChavePix
import br.com.zup.chavePix.ChavePixRepository
import br.com.zup.contas.ContaCliente
import br.com.zup.contas.ContaRepository
import br.com.zup.core.validacoes.exception.ChavePixException
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.validation.Validated
import javax.inject.Singleton
import javax.transaction.Transactional
import javax.validation.Valid

@Validated
@Singleton
class CadastraPixServece(
    val chavePixRepository: ChavePixRepository,
    val contaRepository: ContaRepository,
    val desafioPix: DesafioPix,
    val contaCliente: ContaCliente
) {
    @Transactional
    fun registrar(@Valid novaChave: NovaChavePix?): ChavePix{
        //pesquisa a chave
        var chave = chavePixRepository.findByChave(novaChave?.chaveCadastrada!!)

        if (chave.isPresent){
            throw ChavePixException("Chave Pix ${novaChave?.chaveCadastrada} existente")
        }

val conta = contaRepository.findByContaId(novaChave.clientId)
        val contaCriada = if (conta.isPresent) conta.get()
        else contaCliente.buscar(novaChave.clientId, novaChave?.tipoConta?.name!!)
            .let { contaResponse -> contaRepository.save(contaResponse.paraConta()) }

        try {
            var chaveGerada = novaChave.paraChave(contaCriada)
                .let { chavePix -> chavePixRepository.save(chavePix) }

            val createPixResponse = novaChave?.paraChaveRequest(contaCriada)
                .let { chavePix -> keyManagerPixClient.cadastra(chavePix) }


            chaveGerada = chaveGerada.let { chavePix ->
                novaChave.chaveCadastrada = createPixResponse.key
                chavePix.criadaEm = chaveResponse.createdAt
                chavePixRepository.update(chavePix)
            }

            return chaveCriada
        } catch (e: HttpClientResponseException) {
            throw ChavePixException("A chave pix ${novaChave.chaveCadastrada} já está cadatrada")
        }

    }
}