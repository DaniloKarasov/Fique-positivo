package br.com.fiquepositivo.api.controller;

import br.com.fiquepositivo.api.dto.input.GastoRequest;
import br.com.fiquepositivo.api.dto.output.GastoDTO;
import br.com.fiquepositivo.api.mapper.GastoMapper;
import br.com.fiquepositivo.domain.exceptions.IdNaoCadastradoException;
import br.com.fiquepositivo.domain.model.FormaPagamento;
import br.com.fiquepositivo.domain.model.Gasto;
import br.com.fiquepositivo.domain.model.Pessoa;
import br.com.fiquepositivo.domain.service.GastoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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

    @Test
    void testListar() throws Exception {
        List<Gasto> listEntity = List.of(criarGasto(1), criarGasto(2));
        List<GastoDTO> listDto = List.of(criarGastoDTO(1), criarGastoDTO(2));

        when(gastoService.listar()).thenReturn(listEntity);
        when(gastoMapper.toDtoList(listEntity)).thenReturn(listDto);

        mockMvc.perform(get("/gastos")).andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(listDto)));


    }

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

    @Test
    void testBuscarNaoEncontrado() throws Exception {
        Integer idInvalido = 3;
        String mensagemErro = "Não existe gasto com id %s.";

        when(gastoService.buscar(idInvalido)).thenThrow(new IdNaoCadastradoException(mensagemErro));

        mockMvc.perform(get("/gastos/{id}", idInvalido)).andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    void adicionar() {
        Integer num = 2;
        System.out.println("teste %s teste, num");
    }

    @Test
    void atualizar() {
    }

    @Test
    void excluir() {
    }

    private Gasto criarGasto(Integer id) {
        return new Gasto(id, 3000.0, FormaPagamento.PIX, LocalDate.of(2023, 3, 28),
                "Goiânia", "Notebook", true, new Pessoa(1, null, null, null));
    }

    private GastoDTO criarGastoDTO(Integer id) {
        return new GastoDTO(id, 3000.0, FormaPagamento.PIX, LocalDate.of(2023, 3, 28),
                "Goiânia", "Notebook", true, 1);
    }

    private GastoRequest criarGastoRequest(Integer id) {
        return new GastoRequest(3000.0, FormaPagamento.PIX, LocalDate.of(2023, 3, 28),
                "Goiânia", "Notebook", true, 1);
    }
}