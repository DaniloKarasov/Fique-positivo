package br.com.fiquepositivo.controller;

import br.com.fiquepositivo.model.Pessoa;
import br.com.fiquepositivo.service.PessoaService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PessoaController.class)
class PessoaControllerTest {

    @MockBean
    private PessoaService pessoaService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testListar() throws Exception {
        Pessoa pessoa1 = new Pessoa(1, "Renato", 3500.0, "Segurança");
        Pessoa pessoa2 = new Pessoa(2, "Marcelo", 3000.0, "Motorista");
        List<Pessoa> list = new ArrayList<>();
        list.add(pessoa1);
        list.add(pessoa2);

        when(pessoaService.listar()).thenReturn(list);

        mockMvc.perform(get("/pessoas")).
                andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(list)));

    }

    @Test
    void testBuscar() throws Exception {
        Pessoa pessoa = new Pessoa(3, "Murilo", 2500.0, "Pedreiro");

        when(pessoaService.buscar(3)).thenReturn(ResponseEntity.ok(pessoa));

        mockMvc.perform(get("/pessoas/3"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(pessoa)));
    }
}