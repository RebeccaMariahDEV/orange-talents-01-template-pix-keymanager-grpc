package br.com.zup.core.exception

import java.lang.RuntimeException

class AcessoNegadoException(message: String = "Acesso negado"): RuntimeException(message){

}
