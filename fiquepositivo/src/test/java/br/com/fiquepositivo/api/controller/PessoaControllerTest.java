package br.com.fiquepositivo.api.controller;

import br.com.fiquepositivo.api.dto.input.PessoaRequest;
import br.com.fiquepositivo.api.dto.output.PessoaDTO;
import br.com.fiquepositivo.api.mapper.PessoaMapper;
import br.com.fiquepositivo.domain.exceptions.ConflitoDeDadosException;
import br.com.fiquepositivo.domain.exceptions.IdNaoCadastradoException;
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

    @MockBean
    private PessoaMapper pessoaMapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testListar() throws Exception {
        PessoaDTO pessoaDto1 = new PessoaDTO(1, "Renato", 3500.0, "Segurança");
        PessoaDTO pessoaDto2 = new PessoaDTO(2, "Marcelo", 3000.0, "Motorista");
        List<PessoaDTO> list = new ArrayList<>();
        list.add(pessoaDto1);
        list.add(pessoaDto2);

        when(pessoaMapper.toDtoList(pessoaService.listar())).thenReturn(list);

        mockMvc.perform(get("/pessoas")).andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(list)));

    }

    @Test
    void testBuscar() throws Exception {
        Integer id = 3;
        PessoaDTO pessoaDto = new PessoaDTO(id, "Murilo", 2500.0, "Pedreiro");

        when(pessoaMapper.toDto(any())).thenReturn(pessoaDto);

        mockMvc.perform(get("/pessoas/{id}", id))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(pessoaDto)));

        verify(pessoaMapper).toDto(any());
    }

    @Test
    void testBuscarNaoEncontrado() throws Exception {
        Integer idFalse = 5;
        String mensagemErro = String.format("Não existe pessoa com id %s.", idFalse);

        when(pessoaService.buscar(idFalse)).thenThrow(
                new IdNaoCadastradoException(mensagemErro));

        mockMvc.perform(get("/pessoas/{idFalse}", idFalse))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value(mensagemErro));
    }

    @Test
    void testAdicionar() throws Exception {
        PessoaRequest pessoaInput = new PessoaRequest("Sara", 2000.0, "Farmaceutica");
        PessoaDTO pessoaCreated = new PessoaDTO(1, "Sara", 2000.0, "Farmaceutica");

        when(pessoaMapper.toDto(pessoaService.salvar(pessoaMapper.toEntity(pessoaInput)))).thenReturn(pessoaCreated);

        mockMvc.perform(post("/pessoas").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(pessoaInput)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(pessoaCreated.id()))
                .andExpect(jsonPath("$.nome").value(pessoaCreated.nome()))
                .andExpect(jsonPath("$.rendaMensal").value(pessoaCreated.rendaMensal()))
                .andExpect(jsonPath("$.profissao").value(pessoaCreated.profissao()));

    }

    @Test
    void testAtualizar() throws Exception {
        Integer id = 1;
        PessoaRequest pessoaInput = new PessoaRequest("Marcelo", 3500.0, "Operador de máquinas");
        PessoaDTO pessoaAtualizada = new PessoaDTO(id, "Marcelo", 3500.0, "Operador de máquinas");

        when(pessoaMapper.toDto(any())).thenReturn(pessoaAtualizada);

        mockMvc.perform(put("/pessoas/{id}", id).contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(pessoaInput)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(pessoaAtualizada.id()))
                .andExpect(jsonPath("$.nome").value(pessoaAtualizada.nome()))
                .andExpect(jsonPath("$.rendaMensal").value(pessoaAtualizada.rendaMensal()))
                .andExpect(jsonPath("$.profissao").value(pessoaAtualizada.profissao()));

        verify(pessoaMapper).toDto(any());

    }

    @Test
    void testAtualizarNaoEncontrado() throws Exception {
        Integer idInvalido = 1;
        PessoaRequest pessoaInput = new PessoaRequest("Marcelo", 3500.0, "Operador de máquinas");
        String mensagemErro = String.format("Não existe pessoa com id %s.", idInvalido);
        when(pessoaMapper.toDto(any())).thenThrow(new IdNaoCadastradoException(mensagemErro));

        mockMvc.perform(put("/pessoas/{id}", idInvalido).contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(pessoaInput)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value(mensagemErro));

        verify(pessoaMapper).toDto(any());
    }

    @Test
    void testAtualizarParcialmente() throws Exception {
        Integer id = 1;
        String jsonParcial = "{ \"profissao\": \"Analista\" }";

        PessoaDTO pessoaAtualizada = new PessoaDTO(id, "Sara", 2500.0, "Analista");

        when(pessoaMapper.toDto(any())).thenReturn(pessoaAtualizada);

        mockMvc.perform(patch("/pessoas/{id}", id).contentType(MediaType.APPLICATION_JSON).content(jsonParcial))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(pessoaAtualizada.id()))
                .andExpect(jsonPath("$.nome").value(pessoaAtualizada.nome()))
                .andExpect(jsonPath("$.rendaMensal").value(pessoaAtualizada.rendaMensal()))
                .andExpect(jsonPath("$.profissao").value(pessoaAtualizada.profissao()));

        verify(pessoaMapper).toDto(any());
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
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value(mensagemErro));

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
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value(mensagemErro));

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
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.message").value(mensagemErro));
        verify(pessoaService).excluir(idConflito);
    }
}