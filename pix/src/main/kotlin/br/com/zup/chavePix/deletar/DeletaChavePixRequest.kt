package br.com.zup.chavePix.deletar

import br.com.zup.core.validacoes.seguranca.ValidUUID
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

data class DeletaChavePixRequest(
    @field:NotNull
    val idChave: String,

    @ValidUUID
    @field:NotBlank
    val idCliente: Long
)
data class DeletarChaveXMLRequest(
    val key: String,
    val participant: String
)
data class DeletarChaveXMLProblema(
    val type: String,
    val status: Int,
    val title: String,
    val detail: String,
    val violations: List<DeletarChaveXMLProblemaDetail>
)
data class DeletarChaveXMLProblemaDetail(
    val field: String,
    val message: String
)