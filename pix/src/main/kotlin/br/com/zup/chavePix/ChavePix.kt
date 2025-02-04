package br.com.zup.chavePix

import br.com.zup.contas.Conta
import java.time.LocalDate
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.ManyToOne
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

@Entity
data class ChavePix(
    @field:NotBlank @field:Size(max = 77) var chaveCadastrada: String,
    @field:NotNull val tipo: String,
    @field:ManyToOne val conta: Conta
) {
    @Id
    @GeneratedValue
    val id: Long? = null

    var criadaEm: LocalDate? = null

//    val atualizadaEm: LocalDate = LocalDate

    fun pertence(clienteId: String): Boolean{
        return this.conta.titular?.id.equals(clienteId)
    }
}