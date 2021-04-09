package br.com.zup.core.validacoes.exception

import java.lang.RuntimeException

class ChavePixInvalidaException(message: String = "chave pix inválida"): RuntimeException(message) {
}