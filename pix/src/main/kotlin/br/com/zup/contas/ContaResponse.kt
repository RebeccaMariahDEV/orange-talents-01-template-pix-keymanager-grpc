package br.com.zup.contas

class ContaResponse(
    val agencia: String,
    val numero: String,
    val tipo: TipoConta,
    val instituicao: InstituicaoResponse,
    val titular: TitularResponse
) {
    fun paraConta(): Conta {
        return Conta(
            agencia,
            numero,
            Instituicao(instituicao.nome, instituicao.ispb),
            Titular(titular.id, titular.nome, titular.cpf), tipo)
    }
}
