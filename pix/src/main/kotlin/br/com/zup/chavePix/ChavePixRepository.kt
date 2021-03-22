package br.com.zup.chavePix

import io.micronaut.data.jpa.repository.JpaRepository

interface ChavePixRepository: JpaRepository<ChavePix, Long> {
}