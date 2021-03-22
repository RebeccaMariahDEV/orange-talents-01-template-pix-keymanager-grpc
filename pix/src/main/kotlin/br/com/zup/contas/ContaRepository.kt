package br.com.zup.contas

import io.micronaut.data.jpa.repository.JpaRepository
import java.util.*

interface ContaRepository: JpaRepository<Conta, Long> {
    fun findByContaId(clientId: String): Optional<Conta>
}