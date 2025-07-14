package br.com.fiquepositivo.api.controller;

import br.com.fiquepositivo.api.dto.input.GastoRequest;
import br.com.fiquepositivo.api.dto.output.GastoDTO;
import br.com.fiquepositivo.api.exceptionhandler.MensagensErro;
import br.com.fiquepositivo.api.mapper.GastoMapper;
import br.com.fiquepositivo.domain.exceptions.IdNaoCadastradoException;
import br.com.fiquepositivo.domain.model.FormaPagamento;
import br.com.fiquepositivo.domain.model.Gasto;
import br.com.fiquepositivo.domain.model.Pessoa;
import br.com.fiquepositivo.domain.service.GastoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(GastoController.class)
class GastoControllerTest {

    @MockBean
    private GastoService gastoService;

    @MockBean
    private GastoMapper gastoMapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @DisplayName("Deve retornar uma lista de gastos e status 200")
    @Test
    void testListar() throws Exception {
        List<Gasto> listEntity = List.of(criarGasto(1), criarGasto(2));
        List<GastoDTO> listDto = List.of(criarGastoDTO(1), criarGastoDTO(2));

        when(gastoService.listar()).thenReturn(listEntity);
        when(gastoMapper.toDtoList(listEntity)).thenReturn(listDto);

        mockMvc.perform(get("/gastos")).andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(listDto)));


    }

    @DisplayName("Deve retornar um gasto no corpo e status 200")
    @Test
    void testBuscar() throws Exception {
        Integer id = 1;
        Gasto gasto = criarGasto(id);
        GastoDTO gastoDTO = criarGastoDTO(id);

        when(gastoService.buscar(id)).thenReturn(gasto);
        when(gastoMapper.toDto(gasto)).thenReturn(gastoDTO);

        mockMvc.perform(get("/gastos/{id}", id)).andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(gastoDTO)));
        verify(gastoService).buscar(id);
        verify(gastoMapper).toDto(gasto);
    }

    @DisplayName("Deve retornar status 404 e mensagem no corpo (GET)")
    @Test
    void testBuscarNaoEncontrado() throws Exception {
        Integer idInvalido = 3;
        String mensagemErro = String.format(MensagensErro.ERRO_GASTO_NAO_ENCONTRADO, idInvalido);

        when(gastoService.buscar(idInvalido)).thenThrow(new IdNaoCadastradoException(mensagemErro));

        mockMvc.perform(get("/gastos/{id}", idInvalido)).andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value(mensagemErro));
    }

    @DisplayName("Deve retornar status 201 e o gasto salvo no corpo")
    @Test
    void testAdicionar() throws Exception {

        GastoRequest gastoInput = criarGastoRequest();
        Gasto gastoEntity = criarGasto(1);
        GastoDTO gastoDTO = criarGastoDTO(1);

        when(gastoMapper.toEntity(gastoInput)).thenReturn(gastoEntity);
        when(gastoService.salvar(gastoEntity)).thenReturn(gastoEntity);
        when(gastoMapper.toDto(gastoEntity)).thenReturn(gastoDTO);

        mockMvc.perform(post("/gastos").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(gastoInput)))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(gastoDTO)));

    }

    @DisplayName("Deve retornar o gasto atualizado no corpo e status 200")
    @Test
    void atualizar() throws Exception {
        Integer id = 1;
        GastoDTO gastoDTO = criarGastoDTO(id);
        Gasto gastoEntity = criarGasto(id);
        GastoRequest gastoRequest = criarGastoRequest();

        when(gastoMapper.toEntity(gastoRequest)).thenReturn(gastoEntity);
        when(gastoService.atualizar(id, gastoEntity)).thenReturn(gastoEntity);
        when(gastoMapper.toDto(gastoEntity)).thenReturn(gastoDTO);

        mockMvc.perform(put("/gastos/{id}", id).contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(gastoRequest))).andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(gastoDTO)));
    }

    @DisplayName("Deve retornar status 404 e mensagem de erro no corpo (PUT)")
    @Test
    void testAtualizarNaoEncontrado() throws Exception {
        Integer idInvalido = 2;
        GastoRequest gastoRequest = criarGastoRequest();
        Gasto gastoEntity = criarGasto(null);

        when(gastoMapper.toEntity(gastoRequest)).thenReturn(gastoEntity);
        when(gastoService.atualizar(idInvalido, gastoEntity)).thenThrow(
                new IdNaoCadastradoException(String.format(MensagensErro.ERRO_GASTO_NAO_ENCONTRADO, idInvalido)));

        mockMvc.perform(put("/gastos/{id}", idInvalido).contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(gastoRequest))).andExpect(status().isNotFound())
                .andExpect(
                        jsonPath("$.message").value(
                                String.format(MensagensErro.ERRO_GASTO_NAO_ENCONTRADO, idInvalido)));
    }

    @DisplayName("Deve retornar status 204")
    @Test
    void testExcluir() throws Exception {
        Integer id = 1;

        doNothing().when(gastoService).excluir(id);

        mockMvc.perform(delete("/gastos/{id}", id)).andExpect(status().isNoContent()).andExpect(content().string(""));
        ;

        verify(gastoService).excluir(id);
    }

    @DisplayName("Deve retornar status 404 e mensagem de erro no corpo (DELETE)")
    @Test
    void testExcluirNaoEncontrado() throws Exception {
        Integer idInvalido = 2;

        doThrow(new IdNaoCadastradoException(String.format(MensagensErro.ERRO_GASTO_NAO_ENCONTRADO, idInvalido))).when(
                gastoService).excluir(idInvalido);

        mockMvc.perform(delete("/gastos/{id}", idInvalido)).andExpect(status().isNotFound());

        verify(gastoService).excluir(idInvalido);
    }

    private Gasto criarGasto(Integer id) {
        return new Gasto(id, 3000.0, FormaPagamento.PIX, LocalDate.of(2023, 3, 28),
                "Goiânia", "Notebook", true, new Pessoa(1, null, null, null));
    }

    private GastoDTO criarGastoDTO(Integer id) {
        return new GastoDTO(id, 3000.0, FormaPagamento.PIX, LocalDate.of(2023, 3, 28),
                "Goiânia", "Notebook", true, 1);
    }

    private GastoRequest criarGastoRequest() {
        return new GastoRequest(3000.0, FormaPagamento.PIX, LocalDate.of(2023, 3, 28),
                "Goiânia", "Notebook", true, 1);
    }
}