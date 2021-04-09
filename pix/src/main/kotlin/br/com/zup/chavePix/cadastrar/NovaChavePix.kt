package br.com.zup.grpc.cadastrar

import br.com.zup.chavePix.ChavePix
import br.com.zup.chavePix.TipoChave
import br.com.zup.contas.Conta
import br.com.zup.contas.TipoConta
import br.com.zup.core.validacoes.seguranca.ValidUUID
import br.com.zup.core.validacoes.ValidaChavePix
import io.micronaut.core.annotation.Introspected
import java.time.LocalDateTime
import java.util.*
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size


@ValidaChavePix
@Introspected
data class NovaChavePix (
    @field:ValidUUID
    @field:NotBlank
    val idcliente: String,
    @field:NotNull
    val tipoChave: TipoChave?,
    @field:Size(max = 77)
    var chaveCadastrada: String,
    @field:NotNull
    val tipoConta: TipoConta?
        ){

    fun paraChave(conta: Conta): ChavePix{
        return ChavePix(
            chaveCadastrada = if (tipoChave == TipoChave.CHAVEALEATORIA) UUID.randomUUID().toString() else chaveCadastrada,
            tipo =  tipoConta?.name!!,
            conta = conta
        )
    }
    fun paraChaveRequest(conta: Conta): NovaChaveRequest {
        return NovaChaveRequest(
            KeyType.getKeyType(tipoChave!!),
            chaveCadastrada,
            BankAccount(
                conta?.instituicao!!.ispb, conta?.agencia!!, conta?.numero!!,
                AccountType.getAccountType(conta?.tipo!!)
            ),
            Owner("NATURAL_PERSON", conta.titular?.nome!!, conta?.titular!!.cpf)
        )
    }
}
data class NovaChaveRequest(
    val keyType: KeyType,
    val key: String,
    val bankAccount: BankAccount,
    val owner: Owner
)
data class NovaChaveResponse(
    val keyType: KeyType,
    val key: String,
    val bankAccount: BankAccount,
    val owner: Owner,
    val createdAt: LocalDateTime
)
data class Owner(
    val type: String,
    val name: String,
    val taxIdNumber: String
)
data class BankAccount(
    val participant: String,
    val branch: String,
    val accountNumber: String,
    val accountType: AccountType
)
enum class AccountType {
    CACC, SVGS;

    companion object {
        fun getAccountType(tipoConta: TipoConta): AccountType {
            if("CONTA_CORRENTE".equals(tipoConta.name)){
                return CACC
            }
            return SVGS
        }
    }
}
enum class KeyType {

    CPF, CNPJ, PHONE, EMAIL, RANDOM;

    companion object {
        fun getKeyType(tipoDaChave: TipoChave): KeyType {
            if (CPF.name.equals(tipoDaChave.name)){
                return CPF
            }else if(CNPJ.name.equals(tipoDaChave.name)){
                return CNPJ
            }else if("TELEFONE_CELULAR".equals(tipoDaChave.name.toString())){
                return PHONE
            }else if(EMAIL.name.equals(tipoDaChave.name)){
                return EMAIL
            }

            return RANDOM
        }
    }

}