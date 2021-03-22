package br.com.zup.contas

import io.micronaut.data.jpa.repository.JpaRepository

interface ContaRepository: JpaRepository<Conta, Long> {
}