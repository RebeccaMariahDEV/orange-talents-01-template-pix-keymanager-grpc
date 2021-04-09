package br.com.zup.chavePix.cadastrar

import br.com.zup.PixRequest
import br.com.zup.TipoChave
import br.com.zup.TiposConta
import br.com.zup.cadastraChavePixGrpcGrpc
import br.com.zup.chavePix.ChavePixCliente
import br.com.zup.chavePix.ChavePixRepository
import br.com.zup.contas.*
import br.com.zup.grpc.cadastrar.*
import io.grpc.ManagedChannel
import io.micronaut.context.annotation.Factory
import io.micronaut.grpc.annotation.GrpcChannel
import io.micronaut.grpc.server.GrpcServerChannel
import io.micronaut.test.annotation.MockBean
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.hibernate.validator.internal.util.Contracts.assertNotNull
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import java.time.LocalDateTime
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@MicronautTest(transactional = false)//para não commitar as transaçoes do grpc, outros podem habilitar
internal class CadastraChavePixEndpointTest(
    @Inject val grpc: cadastraChavePixGrpcGrpc.cadastraChavePixGrpcBlockingStub,
    @Inject var repository: ChavePixRepository
) {
    @Inject
    lateinit var contaCliente: ContaCliente

    @Inject
    lateinit var chavePixCliente: ChavePixCliente

    @BeforeEach
    fun setUp(){
        repository.deleteAll()
    }

    @MockBean(ContaCliente::class)
    fun contaCliente(): ContaCliente?{
        return Mockito.mock(ContaCliente::class.java)
    }

    @MockBean(ChavePixCliente::class)
    fun chavePixCliente(): ChavePixCliente?{
        return Mockito.mock(ChavePixCliente::class.java)
    }

    @Factory
    class GrpcClienteFactory{
        @Singleton
        fun cadastrar(@GrpcChannel(GrpcServerChannel.NAME) channel: ManagedChannel):
                cadastraChavePixGrpcGrpc.cadastraChavePixGrpcBlockingStub{
            return cadastraChavePixGrpcGrpc.newBlockingStub(channel)
        }
    }

    @Test
    @DisplayName("deve cadastrar uma chave pix")
    //fun ´deve cadastrar chave pix´(){}
    fun cadastroTeste() {

        //cenario
        val teste = UUID.randomUUID().toString()

        Mockito.`when`(contaCliente.buscar(teste, "CONTA_CORRENTE")).thenReturn(ContaResponse(
            agencia = "0405",
            numero = "590396",
            tipo = TipoConta.CONTA_CORRENTE,
            instituicao = InstituicaoResponse(nome = "Banco Do Brasil", ispb = "1234"),
            titular = TitularResponse(nome = "Luke", cpf = "12345678909")
        ))

        Mockito.`when`(chavePixCliente.cadastra(NovaChaveRequest(KeyType.CPF,
        key = "12345678909",
        bankAccount = BankAccount(participant = "1234",branch = "0405",
            accountNumber = "590396", accountType = AccountType.CACC),
            owner = Owner(type = "", name = "Luke", taxIdNumber = "12345678909"))
        )).thenReturn(NovaChaveResponse(keyType = KeyType.CPF, key = "12345678909",
        bankAccount = BankAccount(participant = "1234",branch = "0405",
            accountNumber = "590396", accountType = AccountType.CACC),
            owner = Owner(type = "", name = "Luke", taxIdNumber = "12345678909"),
            createdAt = LocalDateTime.now())
        )

        //acao
        val response = grpc.cadastrar(
            PixRequest.newBuilder()
                .setIdCliente(teste)
                .setTipoChave(TipoChave.CPF)
                .setChave("12345678909")
                .setTiposConta(TiposConta.CONTA_CORRENTE)
                .build()
        )

        //validacao via assertions
        assertNotNull(response.clietId)
        assertEquals(teste,response.clietId)
        assertTrue(repository.findAll().count() == 0)
    }

}