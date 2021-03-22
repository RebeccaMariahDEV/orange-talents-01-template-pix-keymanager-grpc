package br.com.zup.contas

import javax.persistence.*
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

@Entity
class Conta(
    val tipo: TipoConta,
    @ManyToOne
    val instituicao: Instituicao,
    val agencia: Int,
    val numero: Int,
    val titular: Titular

) {
    @Id
    @GeneratedValue
    val id: Long = 0L

}
