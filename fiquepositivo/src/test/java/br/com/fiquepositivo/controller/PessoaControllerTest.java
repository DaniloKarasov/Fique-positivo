package br.com.fiquepositivo.controller;

import br.com.fiquepositivo.api.controller.PessoaController;
import br.com.fiquepositivo.domain.exceptions.IdNaoCadastradoException;
import br.com.fiquepositivo.domain.model.Pessoa;
import br.com.fiquepositivo.domain.service.PessoaService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
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
        Integer id = 3;
        Pessoa pessoa = new Pessoa(id, "Murilo", 2500.0, "Pedreiro");

        when(pessoaService.buscar(id)).thenReturn(pessoa);

        mockMvc.perform(get("/pessoas/{id}", id))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(pessoa)));
    }

    @Test
    void testBuscarNaoEncontrado() throws Exception {
        Integer idFalse = 5;

        when(pessoaService.buscar(idFalse)).thenThrow(
                new IdNaoCadastradoException(String.format("Não existe pessoa com id %s.", idFalse)));

        mockMvc.perform(get("/pessoas/{idFalse}", idFalse)).andExpect(status().isNotFound())
                .andExpect(content().string(String.format("Não existe pessoa com id %s.", idFalse)));
    }
}