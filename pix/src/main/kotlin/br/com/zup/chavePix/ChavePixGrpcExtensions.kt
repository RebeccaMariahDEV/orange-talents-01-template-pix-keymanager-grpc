package br.com.zup.grpc.cadastrar

fun NovaChavePix.paraChave() : NovaChavePix {
    return NovaChavePix(
        clientId = clientId,
        tipoDaChave = when(tipoDaChave) {
            TipoDaChave.UNKNOWN_TIPO_CHAVE -> null
            else -> br.com.zup.chaves.TipoDaChave.valueOf(tipoDaChave.name)
        },
        chave = chave,
        tipoDaConta = when(tipoDaConta) {
            TipoDaConta.UNKNOWN_TIPO_CONTA -> null
            else -> TipoDaConta.valueOf(tipoDaConta.name)
        }
    )
}