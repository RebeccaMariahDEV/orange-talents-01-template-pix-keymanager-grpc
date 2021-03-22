package br.com.zup.core.validacoes

import br.com.zup.grpc.cadastrar.NovaChavePix
import javax.validation.Constraint
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext
import javax.validation.Payload
import kotlin.reflect.KClass

@MustBeDocumented
@Target(AnnotationTarget.CLASS, AnnotationTarget.TYPE)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [ValidaPixChaveValidada::class])
annotation class ValidaChavePix(
    val message: String = "chave inválida",
    val groups: Array<KClass<Any>> = [],
    val payload: Array<KClass<Payload>> = []
)

class ValidaPixChaveValidada: ConstraintValidator<ValidaChavePix, NovaChavePix>{
    override fun isValid(value: NovaChavePix?,
                         context: ConstraintValidatorContext?): Boolean {
        if (value?.tipoChave == null) return false
        else
            return value.tipoChave.valida(value?.chaveCadastrada)

    }

}
