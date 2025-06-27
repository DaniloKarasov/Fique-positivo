package br.com.fiquepositivo.api.exceptionhandler;

import br.com.fiquepositivo.api.controller.PessoaController;
import br.com.fiquepositivo.domain.service.PessoaService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PessoaController.class)
class ApiExceptionHandlerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    PessoaService pessoaService;

    @Test
    void testErroSintaxeJson() throws Exception {
        String jsonMalFormado = "{\n" +
                "    nome\": \"Hercules\",\n" +
                "    \"profissao\": \"Fazendeiro\",\n" +
                "    \"rendaMensal\": 10000.0\n" +
                "}";
        String mensagemErro = "Erro de sintaxe no JSON enviado.";

        mockMvc.perform(post("/pessoas").contentType(MediaType.APPLICATION_JSON).content(jsonMalFormado))
                .andExpect(status().isBadRequest()).andExpect(content().string(mensagemErro));
    }
}