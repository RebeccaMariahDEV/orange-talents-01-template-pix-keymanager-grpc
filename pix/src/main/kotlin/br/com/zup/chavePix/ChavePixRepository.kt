package br.com.zup.chavePix

import io.micronaut.data.annotation.Query
import io.micronaut.data.annotation.Repository
import io.micronaut.data.jpa.repository.JpaRepository
import java.util.*

@Repository
interface ChavePixRepository: JpaRepository<ChavePix, Long> {

//    @Query("select chave from ChavePix chave where chave.chaveCadastrada = :chaveCadastrada")
    @Query("select * from chave_pix where chave_cadastrada = ':chaveCadastrada'", nativeQuery = true)
    fun findByChaveCadastrada(chaveCadastrada: String): Optional<ChavePix>

    @Query("select case when count(p) > 0 then true else false end from ChavePix p where p.chaveCadastrada = :chaveCadastrada")
    fun existsByChaveCadastrada(chaveCadastrada: String): Boolean

    @Query("select c from ChavePix c where c.conta.titular.id = :titularId")
    fun findByTitularId(titularId: String): List<ChavePix>
}