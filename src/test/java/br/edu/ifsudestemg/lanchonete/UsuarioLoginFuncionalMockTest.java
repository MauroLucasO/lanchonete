package br.edu.ifsudestemg.lanchonete;

import br.edu.ifsudestemg.lanchonete.model.entity.Usuario;

import br.edu.ifsudestemg.lanchonete.model.repository.UsuarioRepository;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.AfterEach;

import org.junit.jupiter.api.BeforeEach;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.http.MediaType;

import org.springframework.test.web.servlet.MockMvc;

import org.springframework.test.web.servlet.ResultActions;

import java.nio.charset.StandardCharsets;

import java.util.ArrayList;

import java.util.HashMap;

import java.util.List;

import java.util.Map;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest

@AutoConfigureMockMvc

class UsuarioLoginFuncionalMockTest {


    private static final String URL_USUARIOS = "/api/v1/usuarios";

    private static final String URL_AUTH = "/api/v1/usuarios/auth";

    private static final String URL_CLIENTES = "/api/v1/clientes";

    private static final String URL_PRODUTOS = "/api/v1/produtos";

    @Autowired

    private MockMvc mockMvc;

    @Autowired

    private UsuarioRepository usuarioRepository;

    @Autowired

    private ObjectMapper objectMapper;

    private final List<String> loginsCriados = new ArrayList<>();

    private String loginMaria;

    private String loginAdmin;

    private String loginInexistente;

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

    private ResultActions cadastrar(String login, String senha, String repeticao, String cpf, boolean admin) throws Exception {

        loginsCriados.add(login);
        Map<String, Object> dadosEnviados = new HashMap<>();
        dadosEnviados.put("login", login);
        dadosEnviados.put("senha", senha);
        dadosEnviados.put("senhaRepeticao", repeticao);
        dadosEnviados.put("cpf", cpf);
        dadosEnviados.put("admin", admin);
        return mockMvc.perform(post(URL_USUARIOS)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dadosEnviados)));
    }

    private ResultActions autenticar(String login, String senha) throws Exception {

        Map<String, Object> dadosEnviados = new HashMap<>();
        dadosEnviados.put("login", login);
        dadosEnviados.put("senha", senha);
        return mockMvc.perform(post(URL_AUTH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dadosEnviados)));
    }

    private String obterToken(String login, String senha) throws Exception {

        String resposta = autenticar(login, senha)
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

        return objectMapper.readTree(resposta).get("token").asText();
    }

    private String textoDaResposta(ResultActions resultado) throws Exception {

        return resultado.andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

    }

    @Test
    void caso01_cadastroValido_retorna201ESenhaCriptografada() throws Exception {

        cadastrar(loginMaria, "senha123", "senha123", "12345678901", false)

                .andExpect(status().isCreated());

        Usuario salvo = usuarioRepository.findByLogin(loginMaria).orElseThrow();
        assertNotEquals("senha123", salvo.getSenha());
        assertTrue(salvo.getSenha().startsWith("$2"));
    }

    @Test
    void caso02_senhaEmBranco_retorna400() throws Exception {

        ResultActions r = cadastrar(loginMaria, "   ", "   ", "12345678901", false)
                .andExpect(status().isBadRequest());
        assertEquals("Senha inválida", textoDaResposta(r));

    }

    @Test
    void caso03_repeticaoDeSenhaNula_retorna400() throws Exception {

        ResultActions r = cadastrar(loginMaria, "senha123", null, "12345678901", false)
                .andExpect(status().isBadRequest());
        assertEquals("Senha inválida", textoDaResposta(r));
    }

    @Test
    void caso04_senhasDiferentes_retorna400() throws Exception {

        ResultActions r = cadastrar(loginMaria, "senha123", "outraSenha", "12345678901", false)
                .andExpect(status().isBadRequest());
        assertEquals("Senhas não conferem", textoDaResposta(r));

    }

    @Test

    void caso05_loginEmBranco_retorna400() throws Exception {

        ResultActions r = cadastrar("  ", "senha123", "senha123", "12345678901", false)
                .andExpect(status().isBadRequest());
        assertEquals("Email inválido", textoDaResposta(r));
    }

    @Test
    void caso06_cpfEmBranco_retorna400() throws Exception {

        ResultActions r = cadastrar(loginMaria, "senha123", "senha123", "", false)
                .andExpect(status().isBadRequest());
        assertEquals("CPF inválido", textoDaResposta(r));

    }

    @Test
    void caso07_loginComCredenciaisCorretas_retornaToken() throws Exception {

        cadastrar(loginMaria, "senha123", "senha123", "12345678901", false)
                .andExpect(status().isCreated());
        String token = obterToken(loginMaria, "senha123");
        assertFalse(token.isBlank());
        assertEquals(3, token.split("\\.").length);
    }

    @Test
    void caso08_loginComSenhaErrada_retorna401() throws Exception {

        cadastrar(loginMaria, "senha123", "senha123", "12345678901", false)
                .andExpect(status().isCreated());
        autenticar(loginMaria, "senhaErrada")
                .andExpect(status().isUnauthorized());

    }

    @Test
    void caso09_loginDeUsuarioInexistente_retorna401() throws Exception {

        autenticar(loginInexistente, "senha123")
                .andExpect(status().isUnauthorized());
    }

    @Test
    void caso10_rotaPublicaDeProdutos_acessivelSemToken() throws Exception {

        mockMvc.perform(get(URL_PRODUTOS))
                .andExpect(status().isOk());
    }

    @Test
    void caso11_rotaDeClientesSemToken_retorna403() throws Exception {

        mockMvc.perform(get(URL_CLIENTES))
                .andExpect(status().isForbidden());
    }

    @Test
    void caso12_rotaDeClientesComTokenDeUsuarioComum_retorna403() throws Exception {

        cadastrar(loginMaria, "senha123", "senha123", "12345678901", false)
                .andExpect(status().isCreated());
        String token = obterToken(loginMaria, "senha123");

        mockMvc.perform(get(URL_CLIENTES).header("Authorization", "Bearer " + token))
                .andExpect(status().isForbidden());
    }

    @Test
    void caso13_rotaDeClientesComTokenDeAdmin_retorna200() throws Exception {

        cadastrar(loginAdmin, "admin123", "admin123", "10987654321", true)
                .andExpect(status().isCreated());
        String token = obterToken(loginAdmin, "admin123");

        mockMvc.perform(get(URL_CLIENTES).header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }

    @Test
    void caso14_tokenInvalido_retorna403() throws Exception {

        mockMvc.perform(get(URL_CLIENTES).header("Authorization", "Bearer token.invalido.qualquer"))
                .andExpect(status().isForbidden());
    }

}