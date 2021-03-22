package br.com.zup.chavePix

import io.micronaut.validation.validator.constraints.EmailValidator


enum class TipoChave {
    CPF {
        //forum de kotlin, voltar e refatorar para algo mais pratico ao fim
        override fun valida(cpf: String): Boolean {
            val cpfClean = cpf.replace(".", "").replace("-", "")

            //tamanho do cpf
            if (cpfClean.length != 11)
                return false

            //## checando os numeros
            try {
                val number = cpfClean.toLong()
            } catch (e: Exception) {
                return false
            }

            //continue
            var dvCurrent10 = cpfClean.substring(9, 10).toInt()
            var dvCurrent11 = cpfClean.substring(10, 11).toInt()

            //
            //a soma dos nove primeiros dígitos determina o décimo dígito
            val cpfNineFirst = IntArray(9)
            var i = 9
            while (i > 0) {
                cpfNineFirst[i - 1] = cpfClean.substring(i - 1, i).toInt()
                i--
            }
            //multiplique os nove dígitos para seus pesos: 10,9..2
            var sumProductNine = IntArray(9)
            var weight = 10
            var position = 0
            while (weight >= 2) {
                sumProductNine[position] = weight * cpfNineFirst[position]
                weight--
                position++
            }
            //Verifique o nono dígito
            var dvForTenthDigit = sumProductNine.sum() % 11
            dvForTenthDigit = 11 - dvForTenthDigit //rule for tenth digit
            if (dvForTenthDigit > 9)
                dvForTenthDigit = 0
            if (dvForTenthDigit != dvCurrent10)
                return false

            // verificar décimo dígito
            var cpfTenFirst = cpfNineFirst.copyOf(10)
            cpfTenFirst[9] = dvCurrent10

            //multiplique os nove dígitos para seus pesos: 10,9..2
            var sumProductTen = IntArray(10)
            var w = 11
            var p = 0
            while (w >= 2) {
                sumProductTen[p] = w * cpfTenFirst[p]
                w--
                p++
            }
            //Verifique o nono dígito
            var dvForeleventhDigit = sumProductTen.sum() % 11
            dvForeleventhDigit = 11 - dvForeleventhDigit //regra para o décimo dígito
            if (dvForeleventhDigit > 9)
                dvForeleventhDigit = 0
            if (dvForeleventhDigit != dvCurrent11)
                return false

            return true
        }
    },
    CELULAR {
        override fun valida(telefone: String): Boolean {
            if (telefone.isNullOrBlank()) return false

            return telefone.matches("^\\+[1-9][0-9]\\d{1,14}$".toRegex())
        }
    },
    EMAIL {
        override fun valida(email: String): Boolean {
            if(email.isNullOrBlank()) return false

            return EmailValidator().run {
                initialize(null)
                isValid(email, null)
            }
        }
    },
    CHAVEALEATORIA {
        override fun valida(chaveAleatoria: String): Boolean {
            return chaveAleatoria.isNullOrBlank()
        }
    };

    abstract fun valida(chave: String): Boolean
}