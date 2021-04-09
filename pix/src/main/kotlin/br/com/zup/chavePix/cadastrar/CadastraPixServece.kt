package br.com.zup.chavePix.cadastrar


import br.com.zup.chavePix.ChavePix
import br.com.zup.chavePix.ChavePixCliente
import br.com.zup.chavePix.ChavePixRepository
import br.com.zup.contas.ContaCliente
import br.com.zup.contas.ContaRepository
import br.com.zup.core.validacoes.exception.ChavePixException
import br.com.zup.grpc.cadastrar.NovaChavePix
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.validation.Validated
import java.time.LocalDate
import javax.inject.Singleton
import javax.transaction.Transactional
import javax.validation.Valid

@Validated
@Singleton
class CadastraPixServece(
    val chavePixRepository: ChavePixRepository,
    val contaRepository: ContaRepository,
    val chavePixCliente: ChavePixCliente,
    val contaCliente: ContaCliente
) {
    @Transactional
    fun registrar(@Valid novaChave: NovaChavePix?): ChavePix {
        //pesquisa a chave
//        val chave = chavePixRepository.findByChaveCadastrada(novaChave?.chaveCadastrada!!)

//        if (chave.isPresent) {
//            throw ChavePixException("Chave Pix ${novaChave?.chaveCadastrada} existente")
//        }

        if (this.chavePixRepository.existsByChaveCadastrada(novaChave?.chaveCadastrada!!)) {
            throw ChavePixException("Chave Pix ${novaChave.chaveCadastrada} existente")
        }

        val conta = contaRepository.findByContaId(novaChave.idcliente)

//        val itauResponse = contaCliente.buscar(novaChave.idcliente, novaChave?.tipoConta?.name!!)
//        val contaTeste = itauResponse.paraConta()
//
//        contaRepository.save(contaTeste)

        val contaCriada = if (conta.isPresent) conta.get()
        else contaCliente.buscar(novaChave.idcliente, novaChave?.tipoConta?.name!!)
            .let { contaResponse -> contaRepository.save(contaResponse.paraConta()) }

        try {
            var chaveGerada = novaChave.paraChave(contaCriada)
                .let { chavePix -> chavePixRepository.save(chavePix) }

            val createPixResponse = novaChave?.paraChaveRequest(contaCriada)
                .let { chavePix -> chavePixCliente.cadastra(chavePix) }


            chaveGerada = chaveGerada.let { chavePix ->
                chavePix.chaveCadastrada = createPixResponse.key
                createPixResponse.createdAt.also { chavePix.criadaEm = LocalDate.now() }
                chavePixRepository.update(chavePix)
            }

            return chaveGerada
        } catch (e: HttpClientResponseException) {
            throw ChavePixException("A chave pix ${novaChave.chaveCadastrada} já está cadatrada")
        }

    }
}

