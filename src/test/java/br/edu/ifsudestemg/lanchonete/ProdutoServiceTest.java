package br.edu.ifsudestemg.lanchonete;

import br.edu.ifsudestemg.lanchonete.exception.RegraNegocioException;
import br.edu.ifsudestemg.lanchonete.model.entity.Produto;
import br.edu.ifsudestemg.lanchonete.service.ProdutoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProdutoServiceTest {

    private ProdutoService service;

    @BeforeEach
    void setUp() {
        service = new ProdutoService(null);
    }

    private Produto produtoValido() {
        Produto produto= new Produto();
        produto.setNome("X-Burger");
        produto.setValor(15.90f);
        produto.setDescricao("Pão, carne e queijo");
        return produto;
    }

    private void assertInvalido(Produto p, String mensagemEsperada) {
        RegraNegocioException ex = assertThrows(RegraNegocioException.class, () -> service.validar(p));
        assertEquals(mensagemEsperada, ex.getMessage());
    }

    @Test
    void Teste1_produtoValido_naoLancaExcecao() {
        assertDoesNotThrow(() -> service.validar(produtoValido()));
    }

    @Test
    void Teste2_produtoNulo() {
        assertInvalido(null, "Produto inválido");
    }

    @Test
    void Teste3_nomeNulo() {
        Produto produto= produtoValido();
        produto.setNome(null);
        assertInvalido(produto, "Nome inválido");
    }

    @Test
    void Teste4_nomeVazio() {
        Produto produto= produtoValido();
        produto.setNome("");
        assertInvalido(produto, "Nome inválido");
    }

    @Test
    void Teste5_nomeSoEspacos() {
        Produto produto= produtoValido();
        produto.setNome("   ");
        assertInvalido(produto, "Nome inválido");
    }

    @Test
    void Teste6_nomeComUmCaraTesteere() {
        Produto produto= produtoValido();
        produto.setNome("A");
        assertInvalido(produto, "Nome inválido");
    }

    @Test
    void Teste7_nomeComDoisCaraTesteeres() {
        Produto produto= produtoValido();
        produto.setNome("AB");
        assertDoesNotThrow(() -> service.validar(produto));
    }

    @Test
    void Teste8_nomeUmCaraTesteereComEspacos() {
        Produto produto= produtoValido();
        produto.setNome("  A  ");
        assertInvalido(produto, "Nome inválido");
    }

    @Test
    void Teste9_valorNulo() {
        Produto produto= produtoValido();
        produto.setValor(null);
        assertInvalido(produto, "Valor inválido");
    }

    @Test
    void Teste10_valorNegativo() {
        Produto produto= produtoValido();
        produto.setValor(-0.01f);
        assertInvalido(produto, "Valor inválido");
    }

    @Test
    void Teste11_valorZero() {
        Produto produto= produtoValido();
        produto.setValor(0f);
        assertInvalido(produto, "Valor inválido");
    }

    @Test
    void Teste12_valorMinimoValido() {
        Produto produto= produtoValido();
        produto.setValor(0.01f);
        assertDoesNotThrow(() -> service.validar(produto));
    }

    @Test
    void Teste13_valorLimiteSuperior() {
        Produto produto= produtoValido();
        produto.setValor(10000f);
        assertDoesNotThrow(() -> service.validar(produto));
    }

    @Test
    void Teste14_valorAcimaDoLimite() {
        Produto produto= produtoValido();
        produto.setValor(10000.01f);
        assertInvalido(produto, "Valor inválido");
    }

    @Test
    void Teste15_descricaoNula() {
        Produto produto= produtoValido();
        produto.setDescricao(null);
        assertInvalido(produto, "Descrição inválida");
    }

    @Test
    void Teste16_descricaoVazia() {
        Produto produto= produtoValido();
        produto.setDescricao("");
        assertInvalido(produto, "Descrição inválida");
    }

    @Test
    void Teste17_descricaoSoEspacos() {
        Produto produto= produtoValido();
        produto.setDescricao("     ");
        assertInvalido(produto, "Descrição inválida");
    }

    @Test
    void Teste18_descricaoComQuatroCaraTesteeres() {
        Produto produto= produtoValido();
        produto.setDescricao("abcd");
        assertInvalido(produto, "Descrição inválida");
    }

    @Test
    void Teste19_descricaoComCincoCaraTesteeres() {
        Produto produto= produtoValido();
        produto.setDescricao("abcde");
        assertDoesNotThrow(() -> service.validar(produto));
    }
}
