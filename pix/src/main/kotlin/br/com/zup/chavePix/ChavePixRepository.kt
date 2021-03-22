package br.com.zup.chavePix

import io.micronaut.data.jpa.repository.JpaRepository
import java.util.*

interface ChavePixRepository: JpaRepository<ChavePix, Long> {
    fun findByChave(chave: Any): Optional<ChavePix>
}