package br.com.zup.chavePix


enum class ChaveUsuario {
    CPF{
        //forum de kotlin, voltar e refatorar para algo mais pratico ao fim
       fun validarCpf(cpf : String) : Boolean{
           val cpfClean = cpf.replace(".", "").replace("-", "")

           //tamanho do cpf
           if (cpfClean.length != 11)
               return false

           //## checando os numeros
           try {
               val number  = cpfClean.toLong()
           }catch (e : Exception){
               return false
           }

           //continue
           var dvCurrent10 = cpfClean.substring(9,10).toInt()
           var dvCurrent11= cpfClean.substring(10,11).toInt()

           //
           //a soma dos nove primeiros dígitos determina o décimo dígito
           val cpfNineFirst = IntArray(9)
           var i = 9
           while (i > 0 ) {
               cpfNineFirst[i-1] = cpfClean.substring(i-1, i).toInt()
               i--
           }
           //multiplique os nove dígitos para seus pesos: 10,9..2
           var sumProductNine = IntArray(9)
           var weight = 10
           var position = 0
           while (weight >= 2){
               sumProductNine[position] = weight * cpfNineFirst[position]
               weight--
               position++
           }
           //Verifique o nono dígito
           var dvForTenthDigit = sumProductNine.sum() % 11
           dvForTenthDigit = 11 - dvForTenthDigit //rule for tenth digit
           if(dvForTenthDigit > 9)
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
           while (w >= 2){
               sumProductTen[p] = w * cpfTenFirst[p]
               w--
               p++
           }
           //Verifique o nono dígito
           var dvForeleventhDigit = sumProductTen.sum() % 11
           dvForeleventhDigit = 11 - dvForeleventhDigit //regra para o décimo dígito
           if(dvForeleventhDigit > 9)
               dvForeleventhDigit = 0
           if (dvForeleventhDigit != dvCurrent11)
               return false

           return true
       }
       },
    TELEFONE{
            fun validarTelefone(){

            }
            },
    EMAIL{
         fun validarEmail(){

         }
         },
    CHAVEALEATORIA{
        fun validarChaveAleatoria(){

        }
    }
}