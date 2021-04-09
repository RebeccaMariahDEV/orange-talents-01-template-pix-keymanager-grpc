package br.com.zup.chavePix.listar

import br.com.zup.core.validacoes.seguranca.ValidUUID
import javax.validation.constraints.NotBlank

class ListaChavePix(
@field:ValidUUID
@field:NotBlank val idCliente: String
) {
}