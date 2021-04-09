package br.com.zup.core.validacoes.exception

import java.lang.RuntimeException

class AcessoNegadoException(message: String = "Acesso negado"): RuntimeException(message){

}
