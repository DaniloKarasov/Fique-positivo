package br.com.fiquepositivo.api.controller;

import br.com.fiquepositivo.domain.exceptions.ConflitoDeDadosException;
import br.com.fiquepositivo.domain.exceptions.IdNaoCadastradoException;
import br.com.fiquepositivo.domain.model.Pessoa;
import br.com.fiquepositivo.domain.service.PessoaService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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

        mockMvc.perform(get("/pessoas")).andExpect(status().isOk())
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

        mockMvc.perform(get("/pessoas/{idFalse}", idFalse))
                .andExpect(status().isNotFound())
                .andExpect(content().string(String.format("Não existe pessoa com id %s.", idFalse)));
    }

    @Test
    void testAdicionar() throws Exception {
        Pessoa pessoaInput = new Pessoa(null, "Sara", 2000.0, "Farmaceutica");
        Pessoa pessoaCreated = new Pessoa(1, "Sara", 2000.0, "Farmaceutica");

        when(pessoaService.salvar(pessoaInput)).thenReturn(pessoaCreated);

        mockMvc.perform(post("/pessoas").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(pessoaInput)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(pessoaCreated.getId()))
                .andExpect(jsonPath("$.nome").value(pessoaCreated.getNome()))
                .andExpect(jsonPath("$.rendaMensal").value(pessoaCreated.getRendaMensal()))
                .andExpect(jsonPath("$.profissao").value(pessoaCreated.getProfissao()));

        verify(pessoaService).salvar(pessoaInput);
    }

    @Test
    void testAtualizar() throws Exception {
        Integer id = 1;
        Pessoa pessoaInput = new Pessoa(null, "Marcelo", 3500.0, "Operador de máquinas");
        Pessoa pessoaAtualizada = new Pessoa(id, "Marcelo", 3500.0, "Operador de máquinas");

        when(pessoaService.atualizar(id, pessoaInput)).thenReturn(pessoaAtualizada);

        mockMvc.perform(put("/pessoas/{id}", id).contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(pessoaInput)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(pessoaAtualizada.getId()))
                .andExpect(jsonPath("$.nome").value(pessoaAtualizada.getNome()))
                .andExpect(jsonPath("$.rendaMensal").value(pessoaAtualizada.getRendaMensal()))
                .andExpect(jsonPath("$.profissao").value(pessoaAtualizada.getProfissao()));

        verify(pessoaService).atualizar(id, pessoaInput);
    }

    @Test
    void testAtualizarNaoEncontrado() throws Exception {
        Integer idInvalido = 1;
        Pessoa pessoaInput = new Pessoa(null, "Marcelo", 3500.0, "Operador de máquinas");
        String mensagemErro = String.format("Não existe pessoa com id %s.", idInvalido);
        when(pessoaService.atualizar(idInvalido, pessoaInput)).thenThrow(new IdNaoCadastradoException(mensagemErro));

        mockMvc.perform(put("/pessoas/{id}", idInvalido).contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(pessoaInput)))
                .andExpect(status().isNotFound())
                .andExpect(content().string(mensagemErro));

        verify(pessoaService).atualizar(idInvalido, pessoaInput);
    }

    @Test
    void testAtualizarParcialmente() throws Exception {
        Integer id = 1;
        String jsonParcial = "{ \"profissao\": \"Analista\" }";

        Pessoa pessoaAtualizada = new Pessoa(id, "Sara", 2500.0, "Analista");

        when(pessoaService.atualizarParcialmente(eq(id), anyMap())).thenReturn(pessoaAtualizada);

        mockMvc.perform(patch("/pessoas/{id}", id).contentType(MediaType.APPLICATION_JSON).content(jsonParcial))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(pessoaAtualizada.getId()))
                .andExpect(jsonPath("$.nome").value(pessoaAtualizada.getNome()))
                .andExpect(jsonPath("$.rendaMensal").value(pessoaAtualizada.getRendaMensal()))
                .andExpect(jsonPath("$.profissao").value(pessoaAtualizada.getProfissao()));

        verify(pessoaService).atualizarParcialmente(eq(id), anyMap());
    }

    @Test
    void testAtualizarParcialmenteNaoEncontrado() throws Exception {
        Integer idInvalido = 1;
        String jsonParcial = "{ \"profissao\": \"Analista\" }";
        String mensagemErro = String.format("Não existe pessoa com id %s.", idInvalido);

        when(pessoaService.atualizarParcialmente(eq(idInvalido), anyMap())).thenThrow(
                new IdNaoCadastradoException(mensagemErro));

        mockMvc.perform(patch("/pessoas/{id}", idInvalido).contentType(MediaType.APPLICATION_JSON).content(jsonParcial))
                .andExpect(status().isNotFound()).andExpect(content().string(mensagemErro));

        verify(pessoaService).atualizarParcialmente(eq(idInvalido), anyMap());
    }

    @Test
    void testExcluir() throws Exception {
        Integer id = 1;
        doNothing().when(pessoaService).excluir(id);
        mockMvc.perform(delete("/pessoas/{id}", id)).andExpect(status().isNoContent());

        verify(pessoaService).excluir(id);
    }

    @Test
    void testExcluirNaoEncontrado() throws Exception {
        Integer idInvalido = 1;
        String mensagemErro = String.format("Não existe pessoa com id %s.", idInvalido);
        doThrow(new IdNaoCadastradoException(mensagemErro)).when(pessoaService).excluir(idInvalido);
        mockMvc.perform(delete("/pessoas/{id}", idInvalido)).andExpect(status().isNotFound())
                .andExpect(content().string(mensagemErro));

        verify(pessoaService).excluir(idInvalido);
    }

    @Test
    void testExcluirConflito() throws Exception {
        Integer idConflito = 2;
        String mensagemErro =
                String.format("A pessoa com o id %s não pode ser excluída porque existe gasto/s vinculado/s a ela.",
                        idConflito);
        doThrow(new ConflitoDeDadosException(mensagemErro)).when(pessoaService).excluir(idConflito);
        mockMvc.perform(delete("/pessoas/{id}", idConflito)).andExpect(status().isConflict())
                .andExpect(content().string(mensagemErro));
        verify(pessoaService).excluir(idConflito);
    }
}