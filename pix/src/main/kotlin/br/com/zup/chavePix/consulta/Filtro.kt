package br.com.zup.chavePix.consulta

import br.com.zup.chavePix.ChavePix
import br.com.zup.chavePix.ChavePixCliente
import br.com.zup.chavePix.ChavePixRepository
import br.com.zup.core.validacoes.exception.AcessoNegadoException
import br.com.zup.core.validacoes.exception.ChavePixInvalidaException
import br.com.zup.core.validacoes.exception.ChaveNaoEncontradaException
import br.com.zup.core.validacoes.seguranca.ValidUUID
import io.micronaut.core.annotation.Introspected
import io.micronaut.http.HttpStatus
import java.lang.IllegalStateException
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

sealed class Filtro {
    abstract fun filtra(
        repository: ChavePixRepository,
        chavePixCliente: ChavePixCliente
    ): ChavePix

    @Introspected
    data class PorPixId(
        @field:NotBlank @field:ValidUUID val idCliente: String,
        @field:NotNull val idChave: Long
    ) : Filtro() {

        fun idChave() = idChave
        fun idCliente() = idCliente

        override fun filtra(
            repository: ChavePixRepository, chavePixCliente: ChavePixCliente
        ): ChavePix {
            val findChave = repository.findById(idChave)
            if (findChave.isEmpty) {
                throw ChaveNaoEncontradaException("Chave pix ${idChave} não encontrada")
            }
            val chavePix = findChave.get()
            if (!chavePix.pertence(idCliente)) {
                throw AcessoNegadoException("Você não tem permissão para acessar essa função")
            }
            return chavePix
        }
    }

    @Introspected
    data class PorChave(@field:NotBlank @Size(max = 77) val chaveCadastrada: String) : Filtro() {
        override fun filtra(
            repository: ChavePixRepository, chavePixCliente: ChavePixCliente
        ): ChavePix {
            val findChave = repository.findByChaveCadastrada(chaveCadastrada)
            if (findChave.isEmpty) {
                throw ChaveNaoEncontradaException("Chave pix ${chaveCadastrada} não encontrada")
            }
            val chavePix = findChave.get()
            val consultaChavePix = chavePixCliente.consulta(chaveCadastrada)

            if (consultaChavePix.status != HttpStatus.OK) {
                ChavePixInvalidaException("Chave ${chaveCadastrada} invalida")
            }

            return chavePix
        }

        @Introspected
        class Invalidado() {
            fun filtra(
                repository: ChavePixRepository, chavePixCliente: ChavePixCliente
            ): ChavePix {
                throw IllegalStateException("Filtro invalido")
            }
        }
    }
}