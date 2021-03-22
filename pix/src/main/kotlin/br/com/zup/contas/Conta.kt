package br.com.zup.contas

import javax.persistence.*

@Entity
class Conta(
    val agencia: String,
    val numero: String,
    @field:ManyToOne(cascade = [CascadeType.PERSIST, CascadeType.MERGE])
    val instituicao: Instituicao?,
    @field:ManyToOne(cascade = [CascadeType.PERSIST, CascadeType.MERGE])
    val titular: Titular?,
    @field: Enumerated(EnumType.STRING)
    var tipo: TipoConta?

) {
    @Id
    @GeneratedValue
    val id: Long = 0L

}
