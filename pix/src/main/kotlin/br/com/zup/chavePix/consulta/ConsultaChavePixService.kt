package br.com.zup.chavePix.consulta

import br.com.zup.chavePix.ChavePix
import br.com.zup.chavePix.ChavePixRepository
import br.com.zup.core.validacoes.exception.AcessoNegadoException
import br.com.zup.core.validacoes.exception.ChaveNaoEncontradaException
import io.micronaut.validation.Validated
import javax.inject.Singleton
import javax.validation.Valid

@Validated
@Singleton
class ConsultaChavePixService(
    val chavePixRepository: ChavePixRepository
) {
    fun consulta(@Valid chaveConsulta: ConsultaChavePix): ChavePix?{
        val findChave = chavePixRepository.findById(chaveConsulta.idChave)
        if (findChave.isEmpty){
            throw ChaveNaoEncontradaException("Chave pix ${chaveConsulta.idChave} não encontrada")
        }

        val chavePix = findChave.get()
        if (!chavePix.pertence(chaveConsulta.idCliente)){
            throw AcessoNegadoException("Você não tem permissão para acessar essa função")
        }

        return chavePix
    }
}