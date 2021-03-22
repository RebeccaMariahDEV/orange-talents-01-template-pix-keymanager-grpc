package br.com.zup.contas

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
class Instituicao (val nome: String,
val ispb: Int){
    @Id
    @GeneratedValue
    var id: Long? = null
}
