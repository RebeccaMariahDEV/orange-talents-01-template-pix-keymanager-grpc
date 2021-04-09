package br.com.zup.chavePix.deletar

import br.com.zup.chavePix.ChavePix
import br.com.zup.chavePix.ChavePixCliente
import br.com.zup.chavePix.ChavePixRepository
import br.com.zup.core.validacoes.exception.ChaveNaoEncontradaException
import io.micronaut.http.HttpStatus
import io.micronaut.validation.Validated
import javax.inject.Singleton
import javax.transaction.Transactional
import javax.validation.Valid

@Validated
@Singleton
class DeletaPixService(
    val chavePixRepository: ChavePixRepository,
    val chavePixCliente: ChavePixCliente
) {

    @Transactional
    fun deletar(@Valid deletarChavePixRequest: DeletaChavePixRequest) {
        val findChave = chavePixRepository.findById(deletarChavePixRequest.idCliente)

        if (findChave.isEmpty) {
            throw ChaveNaoEncontradaException("Chave pix ${deletarChavePixRequest.idChave} nãio encontrada")
        }

        val chavePix = findChave.get()
        val request = DeletarChaveXMLRequest(
            key = chavePix.chaveCadastrada,
            participant = chavePix.conta.instituicao?.ispb!!
        )

        chavePixRepository.delete(chavePix)

        val response = chavePixCliente.deleta(chavePix.chaveCadastrada, request)
        if (response.status != HttpStatus.OK) {
            val erro = response.body() as DeletarChaveXMLProblema?

            val message = if (erro != null) "${erro.title}: ${erro.detail}"
            else "Não foi possivel remover a chave"

            throw Exception(message)
        }

    }

}

