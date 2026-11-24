package br.edu.ifsudestemg.lanchonete;

import br.edu.ifsudestemg.lanchonete.exception.RegraNegocioException;
import br.edu.ifsudestemg.lanchonete.model.entity.Cliente;
import br.edu.ifsudestemg.lanchonete.service.ClienteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ClienteServiceTest {

    private ClienteService service;

    @BeforeEach
    void setUp() {
        service = new ClienteService(null);
    }

    private Cliente clienteValido() {
        Cliente cliente = new Cliente();
        cliente.setNome("Maria Silva");
        cliente.setCpf("12345678901");
        cliente.setLogin("maria@email.com");
        return cliente;
    }

    private void assertInvalido(Cliente cliente, String mensagemEsperada) {
        RegraNegocioException ex = assertThrows(RegraNegocioException.class, () -> service.validar(cliente));
        assertEquals(mensagemEsperada, ex.getMessage());
    }

    @Test
    void Teste1_clienteValido_naoLancaExcecao() {
        assertDoesNotThrow(() -> service.validar(clienteValido()));
    }

    @Test
    void Teste2_clienteNulo() {
        assertInvalido(null, "Cliente inválido");
    }

    @Test
    void Teste3_nomeNulo() {
        Cliente cliente = clienteValido();
        cliente.setNome(null);
        assertInvalido(cliente, "Nome inválido");
    }

    @Test
    void Teste4_nomeVazio() {
        Cliente cliente = clienteValido();
        cliente.setNome("");
        assertInvalido(cliente, "Nome inválido");
    }

    @Test
    void Teste5_nomeSoEspacos() {
        Cliente cliente = clienteValido();
        cliente.setNome("    ");
        assertInvalido(cliente, "Nome inválido");
    }

    @Test
    void Teste6_nomeComDoisCaraTesteeres() {
        Cliente cliente = clienteValido();
        cliente.setNome("Jo");
        assertInvalido(cliente, "Nome inválido");
    }

    @Test
    void Teste7_nomeComTresCaraTesteeres() {
        Cliente cliente = clienteValido();
        cliente.setNome("Ana");
        assertDoesNotThrow(() -> service.validar(cliente));
    }

    @Test
    void Teste8_cpfNulo() {
        Cliente cliente = clienteValido();
        cliente.setCpf(null);
        assertInvalido(cliente, "CPF inválido");
    }

    @Test
    void Teste9_cpfVazio() {
        Cliente cliente = clienteValido();
        cliente.setCpf("");
        assertInvalido(cliente, "CPF inválido");
    }

    @Test
    void Teste10_cpfSoEspacos() {
        Cliente cliente = clienteValido();
        cliente.setCpf("           ");
        assertInvalido(cliente, "CPF inválido");
    }

    @Test
    void Teste11_cpfComDezCaraTesteeres() {
        Cliente cliente = clienteValido();
        cliente.setCpf("1234567890");
        assertInvalido(cliente, "CPF inválido");
    }

    @Test
    void Teste12_cpfComOnzeCaraTesteeres() {
        Cliente cliente = clienteValido();
        cliente.setCpf("98765432100");
        assertDoesNotThrow(() -> service.validar(cliente));
    }

    @Test
    void Teste13_cpfComDozeCaraTesteeres() {
        Cliente cliente = clienteValido();
        cliente.setCpf("123456789012");
        assertInvalido(cliente, "CPF inválido");
    }

    @Test
    void Teste14_loginNulo() {
        Cliente cliente = clienteValido();
        cliente.setLogin(null);
        assertInvalido(cliente, "Email inválido");
    }

    @Test
    void Teste15_loginVazio() {
        Cliente cliente = clienteValido();
        cliente.setLogin("");
        assertInvalido(cliente, "Email inválido");
    }

    @Test
    void Teste16_loginSoEspacos() {
        Cliente cliente = clienteValido();
        cliente.setLogin("   ");
        assertInvalido(cliente, "Email inválido");
    }

    @Test
    void Teste17_loginSemArroba() {
        Cliente cliente = clienteValido();
        cliente.setLogin("maria.email.com");
        assertInvalido(cliente, "Email inválido");
    }

    @Test
    void Teste18_loginSemPonto() {
        Cliente cliente = clienteValido();
        cliente.setLogin("maria@email");
        assertInvalido(cliente, "Email inválido");
    }

    @Test
    void Teste19_loginMinimoValido() {
        Cliente cliente = clienteValido();
        cliente.setLogin("a@b.cliente");
        assertDoesNotThrow(() -> service.validar(cliente));
    }
}
