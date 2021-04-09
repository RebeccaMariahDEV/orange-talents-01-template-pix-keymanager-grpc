package br.com.zup.chavePix.listar

import br.com.zup.chavePix.ChavePix
import br.com.zup.chavePix.ChavePixRepository
import io.micronaut.validation.Validated
import javax.inject.Singleton
import javax.validation.Valid

@Validated
@Singleton
class ListaChaveService (val chavePixRepository: ChavePixRepository){
    fun listar(@Valid dadosParaConsulta: ListaChavePix?): List<ChavePix>{
        return chavePixRepository.findByTitularId(dadosParaConsulta?.idCliente!!)
    }
}