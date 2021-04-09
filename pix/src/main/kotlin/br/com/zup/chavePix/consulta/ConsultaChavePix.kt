package br.com.zup.chavePix.consulta

import br.com.zup.core.validacoes.seguranca.ValidUUID
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

class ConsultaChavePix(
    @field:NotNull
    val idChave: Long,
    @field:ValidUUID
    @field:NotBlank
    val idCliente: String
) {

}
