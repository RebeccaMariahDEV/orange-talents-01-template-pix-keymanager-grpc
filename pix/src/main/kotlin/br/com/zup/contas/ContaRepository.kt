package br.com.zup.contas

import io.micronaut.data.annotation.Query
import io.micronaut.data.annotation.Repository
import io.micronaut.data.jpa.repository.JpaRepository
import java.util.*

@Repository
interface ContaRepository: JpaRepository<Conta, Long> {
    @Query("select conta from Conta conta where conta.titular.id = :clientId")
    fun findByContaId(clientId: String): Optional<Conta>
}