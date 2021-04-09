package br.com.zup.grpc.cadastrar

import br.com.zup.*
import br.com.zup.ConsultaChavePixRequest.FiltroCase.*
import br.com.zup.chavePix.consulta.Filtro
import br.com.zup.chavePix.deletar.DeletaChavePixRequest
import br.com.zup.chavePix.listar.ListaChavePix
import br.com.zup.contas.TipoConta
import com.google.protobuf.Timestamp
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset
import javax.validation.Validator

fun PixRequest.paraChave() : NovaChavePix {
    return NovaChavePix(
        idCliente,
        tipoChave = when(tipoChave) {
            TipoChave.CHAVE_NAO_DECLARADO -> null
            else -> br.com.zup.chavePix.TipoChave.valueOf(tipoChave.name)
        },
        chaveCadastrada = chave,
        tipoConta = when(tiposConta) {
            TiposConta.CONTA_NAO_DECLARADO-> null
            else -> TipoConta.valueOf(tiposConta.name)
        }
    )
}

fun DeletarPixRequest.paraDeletarChave(): DeletaChavePixRequest {
    return DeletaChavePixRequest(
        idChave = idChave ,
        idCliente = idCliente
    )
}

fun ConsultaChavePixRequest.paraFiltro(validator: Validator): Filtro{

    val filtro = when(filtroCase){
        IDCHAVE -> idChave.let{
            Filtro.PorPixId(idCliente = it.idCliente, idChave = it.idChave)
        }
        CHAVE -> Filtro.PorChave(chaveCadastrada = chave)
//        FILTRO_NOT_SET -> Filtro.I
        FILTRO_NOT_SET -> TODO()
    }
    val violations = validator.validate(filtro)
    if (violations.isNotEmpty()){
        throw javax.validation.ConstraintViolationException(violations)
    }
    return filtro
}
fun ListaChavePixRequest.paraListaChavePix(): ListaChavePix {
return ListaChavePix(idCliente)
}

fun LocalDateTime.toTimestampGrpc(): Timestamp{
    val instant: Instant = this.toInstant(ZoneOffset.UTC)

    return Timestamp.newBuilder()
        .setSeconds(instant.epochSecond)
        .setNanos(instant.nano)
        .build()
}
