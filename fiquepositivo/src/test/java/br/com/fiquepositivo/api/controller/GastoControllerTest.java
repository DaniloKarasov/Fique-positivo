package br.com.fiquepositivo.api.controller;

import br.com.fiquepositivo.api.mapper.GastoMapper;
import br.com.fiquepositivo.domain.service.GastoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.*;

@WebMvcTest(GastoController.class)
class GastoControllerTest {

    @MockBean
    GastoService gastoService;

    @MockBean
    GastoMapper gastoMapper;

    @Test
    void testListar() {
    }

    @Test
    void buscar() {
    }

    @Test
    void adicionar() {
    }

    @Test
    void atualizar() {
    }

    @Test
    void excluir() {
    }
}