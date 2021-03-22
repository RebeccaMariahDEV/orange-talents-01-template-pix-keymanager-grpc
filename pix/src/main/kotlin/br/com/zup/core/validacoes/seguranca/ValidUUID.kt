package br.com.zup.core.validacoes.seguranca

import javax.validation.Constraint
import javax.validation.Payload
import javax.validation.ReportAsSingleViolation
import javax.validation.constraints.Pattern
import kotlin.reflect.KClass

//anotação é parte da api pública e inclui na classe ou método
@MustBeDocumented
@ReportAsSingleViolation
@Constraint(validatedBy = [])

//regex de validação de chave estrangeira
@Pattern(regexp = "^[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}$",
    flags = [Pattern.Flag.CASE_INSENSITIVE])
//anotação de execução em runtime
@Retention(AnnotationRetention.RUNTIME)
//possiveis tipos de elementos que podem ser anotados
@Target(AnnotationTarget.FIELD, AnnotationTarget.CONSTRUCTOR,
    AnnotationTarget.PROPERTY,AnnotationTarget.VALUE_PARAMETER)
annotation class ValidUUID(
    val message: String = "não é um formato válido",
    val groups: Array<KClass<Any>> = [],
    val payload: Array<KClass<Payload>> = []
)
