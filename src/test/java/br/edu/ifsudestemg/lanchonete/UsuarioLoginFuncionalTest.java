package br.edu.ifsudestemg.lanchonete;

import br.edu.ifsudestemg.lanchonete.model.entity.Usuario;

import br.edu.ifsudestemg.lanchonete.model.repository.UsuarioRepository;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.AfterEach;

import org.junit.jupiter.api.BeforeEach;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.boot.test.web.client.TestRestTemplate;

import org.springframework.http.HttpEntity;

import org.springframework.http.HttpHeaders;

import org.springframework.http.HttpMethod;

import org.springframework.http.ResponseEntity;

import org.springframework.http.client.SimpleClientHttpRequestFactory;

import java.nio.charset.StandardCharsets;

import java.util.ArrayList;

import java.util.HashMap;

import java.util.List;

import java.util.Map;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)

class UsuarioLoginFuncionalTest {

    private static final String URL_USUARIOS = "/api/v1/usuarios";

    private static final String URL_AUTH = "/api/v1/usuarios/auth";

    private static final String URL_CLIENTES = "/api/v1/clientes";

    private static final String URL_PRODUTOS = "/api/v1/produtos";

    @Autowired

    private TestRestTemplate restTemplate;

    @Autowired

    private UsuarioRepository usuarioRepository;

    @Autowired

    private ObjectMapper objectMapper;

    private final List<String> loginsCriados = new ArrayList<>();

    private String loginMaria;

    private String loginAdmin;

    private String loginInexistente;

    @BeforeEach

    void configurarClienteHttp() {

        SimpleClientHttpRequestFactory fabrica = new SimpleClientHttpRequestFactory();
        fabrica.setOutputStreaming(false);
        restTemplate.getRestTemplate().setRequestFactory(fabrica);

    }

    @BeforeEach

    void gerarLoginsUnicos() {

        String sufixo = UUID.randomUUID().toString().substring(0, 8);

        loginMaria = "maria." + sufixo + "@teste.com";

        loginAdmin = "admin." + sufixo + "@teste.com";

        loginInexistente = "inexistente." + sufixo + "@teste.com";

        loginsCriados.clear();

    }

    @AfterEach

    void removerUsuariosCriadosNoTeste() {

        for (String login : loginsCriados) {
            usuarioRepository.findByLogin(login).ifPresent(usuarioRepository::delete);
        }
    }

    private ResponseEntity<byte[]> cadastrar(String login, String senha, String repeticao, String cpf, boolean admin) {

        loginsCriados.add(login);
        Map<String, Object> dadosEnviados = new HashMap<>();
        dadosEnviados.put("login", login);
        dadosEnviados.put("senha", senha);
        dadosEnviados.put("senhaRepeticao", repeticao);
        dadosEnviados.put("cpf", cpf);
        dadosEnviados.put("admin", admin);
        return restTemplate.postForEntity(URL_USUARIOS, dadosEnviados, byte[].class);
    }



    private ResponseEntity<byte[]> autenticar(String login, String senha) {

        Map<String, Object> dadosEnviados = new HashMap<>();
        dadosEnviados.put("login", login);
        dadosEnviados.put("senha", senha);
        return restTemplate.postForEntity(URL_AUTH, dadosEnviados, byte[].class);

    }

    private ResponseEntity<byte[]> acessar(String url, String token) {

        HttpHeaders cabecalhos = new HttpHeaders();

        if (token != null) {
            cabecalhos.set("Authorization", "Bearer " + token);
        }
        return restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(cabecalhos), byte[].class);
    }



    private String textoDaResposta(ResponseEntity<byte[]> resposta) {

        byte[] corpo = resposta.getBody();
        return corpo == null ? "" : new String(corpo, StandardCharsets.UTF_8);

    }

    private String obterToken(String login, String senha) throws Exception {

        ResponseEntity<byte[]> resposta = autenticar(login, senha);
        assertEquals(200, resposta.getStatusCode().value());
        return objectMapper.readTree(textoDaResposta(resposta)).get("token").asText();
    }

    @Test

    void caso01_cadastroValido_retorna201ESenhaCriptografada() {

        ResponseEntity<byte[]> r = cadastrar(loginMaria, "senha123", "senha123", "12345678901", false);
        assertEquals(201, r.getStatusCode().value());
        Usuario salvo = usuarioRepository.findByLogin(loginMaria).orElseThrow();
        assertNotEquals("senha123", salvo.getSenha());
        assertTrue(salvo.getSenha().startsWith("$2"));

    }

    @Test
    void caso02_senhaEmBranco_retorna400() {

        ResponseEntity<byte[]> r = cadastrar(loginMaria, "   ", "   ", "12345678901", false);
        assertEquals(400, r.getStatusCode().value());
        assertEquals("Senha inválida", textoDaResposta(r));
    }

    @Test
    void caso03_repeticaoDeSenhaNula_retorna400() {

        ResponseEntity<byte[]> r = cadastrar(loginMaria, "senha123", null, "12345678901", false);
        assertEquals(400, r.getStatusCode().value());
        assertEquals("Senha inválida", textoDaResposta(r));
    }

    @Test
    void caso04_senhasDiferentes_retorna400() {

        ResponseEntity<byte[]> r = cadastrar(loginMaria, "senha123", "outraSenha", "12345678901", false);
        assertEquals(400, r.getStatusCode().value());
        assertEquals("Senhas não conferem", textoDaResposta(r));

    }

    @Test
    void caso05_loginEmBranco_retorna400() {

        ResponseEntity<byte[]> r = cadastrar("  ", "senha123", "senha123", "12345678901", false);
        assertEquals(400, r.getStatusCode().value());
        assertEquals("Email inválido", textoDaResposta(r));

    }

    @Test

    void caso06_cpfEmBranco_retorna400() {

        ResponseEntity<byte[]> r = cadastrar(loginMaria, "senha123", "senha123", "", false);

        assertEquals(400, r.getStatusCode().value());

        assertEquals("CPF inválido", textoDaResposta(r));

    }

    @Test
    void caso07_loginComCredenciaisCorretas_retornaToken() throws Exception {

        ResponseEntity<byte[]> cadastro = cadastrar(loginMaria, "senha123", "senha123", "12345678901", false);
        assertEquals(201, cadastro.getStatusCode().value());
        String token = obterToken(loginMaria, "senha123");
        assertFalse(token.isBlank());
        assertEquals(3, token.split("\\.").length);

    }

    @Test
    void caso08_loginComSenhaErrada_retorna401() {

        ResponseEntity<byte[]> cadastro = cadastrar(loginMaria, "senha123", "senha123", "12345678901", false);
        assertEquals(201, cadastro.getStatusCode().value());
        ResponseEntity<byte[]> r = autenticar(loginMaria, "senhaErrada");
        assertEquals(401, r.getStatusCode().value());

    }

    @Test

    void caso09_loginDeUsuarioInexistente_retorna401() {

        ResponseEntity<byte[]> r = autenticar(loginInexistente, "senha123");
        assertEquals(401, r.getStatusCode().value());

    }

    @Test
    void caso10_rotaPublicaDeProdutos_acessivelSemToken() {

        ResponseEntity<byte[]> r = acessar(URL_PRODUTOS, null);
        assertEquals(200, r.getStatusCode().value());
    }

    @Test
    void caso11_rotaDeClientesSemToken_retorna403() {

        ResponseEntity<byte[]> r = acessar(URL_CLIENTES, null);
        assertEquals(403, r.getStatusCode().value());
    }

    @Test
    void caso12_rotaDeClientesComTokenDeUsuarioComum_retorna403() throws Exception {

        ResponseEntity<byte[]> cadastro = cadastrar(loginMaria, "senha123", "senha123", "12345678901", false);
        assertEquals(201, cadastro.getStatusCode().value());
        String token = obterToken(loginMaria, "senha123");
        ResponseEntity<byte[]> r = acessar(URL_CLIENTES, token);
        assertEquals(403, r.getStatusCode().value());
    }

    @Test
    void caso13_rotaDeClientesComTokenDeAdmin_retorna200() throws Exception {

        ResponseEntity<byte[]> cadastro = cadastrar(loginAdmin, "admin123", "admin123", "10987654321", true);
        assertEquals(201, cadastro.getStatusCode().value());
        String token = obterToken(loginAdmin, "admin123");
        ResponseEntity<byte[]> r = acessar(URL_CLIENTES, token);
        assertEquals(200, r.getStatusCode().value());

    }

    @Test
    void caso14_tokenInvalido_retorna403() {

        ResponseEntity<byte[]> r = acessar(URL_CLIENTES, "token.invalido.qualquer");
        assertEquals(403, r.getStatusCode().value());
    }
}