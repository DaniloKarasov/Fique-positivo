package br.com.fiquepositivo.domain.service;

import br.com.fiquepositivo.domain.exceptions.IdNaoCadastradoException;
import br.com.fiquepositivo.domain.model.FormaPagamento;
import br.com.fiquepositivo.domain.model.Gasto;
import br.com.fiquepositivo.domain.model.Pessoa;
import br.com.fiquepositivo.domain.repository.GastoRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GastoServiceTest {

    @Mock
    GastoRepository gastoRepository;

    @Mock
    PessoaService pessoaService;

    @InjectMocks
    GastoService gastoService;

    @DisplayName("Deve retornar um List<Gasto>")
    @Test
    void testListar() {
        Gasto gasto1 = criarGasto(1);
        Gasto gasto2 = criarGasto(2);
        List<Gasto> gastoList = List.of(gasto1, gasto2);

        when(gastoRepository.findAll()).thenReturn(gastoList);

        List<Gasto> result = gastoService.listar();

        assertEquals(gastoList, result);

    }

    @DisplayName("Deve retornar um Gasto")
    @Test
    void testBuscar() {
        Integer id = 1;
        Gasto gasto = criarGasto(1);

        when(gastoRepository.findById(id)).thenReturn(Optional.of(gasto));

        Gasto result = gastoService.buscar(id);

        assertEquals(gasto, result);


    }

    @DisplayName("Deve lançar IdNaoCadastradoException ao fazer busca com id Inexistente")
    @Test
    void testBuscarNaoEncontrado() {
        Integer idInexistente = 4;

        when(gastoRepository.findById(idInexistente)).thenReturn(Optional.empty());
        assertThrows(IdNaoCadastradoException.class, () -> gastoService.buscar(idInexistente));
        verify(gastoRepository).findById(idInexistente);
    }

    @DisplayName("Deve retornar o gasto salvo")
    @Test
    void testSalvar() {
        Gasto gasto = criarGasto(null);
        Gasto gastoCriado = criarGasto(1);

        when(pessoaService.buscar(gasto.getPessoa().getId())).thenReturn(gasto.getPessoa());
        when(gastoRepository.save(gasto)).thenReturn(gastoCriado);

        Gasto result = gastoService.salvar(gasto);

        assertEquals(gastoCriado, result);
    }

    @DisplayName("Deve retornar o gasto atualizado")
    @Test
    void testAtualizar() {
        Integer id = 1;
        Gasto gastoExistente = criarGasto(1);
        Gasto gastoAtualizado = new Gasto(null, 3000.0, FormaPagamento.DINHEIRO, LocalDate.of(2023, 3, 28),
                "Goiânia", "Notebook", false, new Pessoa(1, "Jorge", 3000.0, "barbeiro"));
        Gasto gastoPersistido = new Gasto(1, 3000.0, FormaPagamento.DINHEIRO, LocalDate.of(2023, 3, 28),
                "Goiânia", "Notebook", false, new Pessoa(1, "Jorge", 3000.0, "barbeiro"));

        when(gastoRepository.findById(id)).thenReturn(Optional.of(gastoExistente));
        when(pessoaService.buscar(any(Integer.class))).thenReturn(gastoExistente.getPessoa());
        when(gastoRepository.save(gastoExistente)).thenReturn(gastoPersistido);

        Gasto result = gastoService.atualizar(id, gastoAtualizado);

        assertEquals(gastoPersistido, result);

        verify(pessoaService).buscar(any(Integer.class));
    }

    @DisplayName("Deve lançar IdNaoCadastradoException ao tentar atualizar um id inexistente")
    @Test
    void testAtualizarNaoEncontrado() {
        Integer idInexistente = 5;
        Gasto gasto = criarGasto(null);

        when(gastoRepository.findById(idInexistente)).thenReturn(Optional.empty());
        assertThrows(IdNaoCadastradoException.class, () -> gastoService.atualizar(idInexistente, gasto));

        verify(gastoRepository, never()).save(any(Gasto.class));
    }

    @DisplayName("Deve excluir um gasto e não retornar nada")
    @Test
    void testExcluir() {
        Integer id = 1;
        Gasto gasto = criarGasto(1);

        when(gastoRepository.findById(id)).thenReturn(Optional.of(gasto));
        doNothing().when(gastoRepository).delete(gasto);

        gastoService.excluir(id);

        verify(gastoRepository).delete(gasto);
    }

    @DisplayName("Deve lançar IdNaoCadastradoException ao tentar excluir com id inexistente")
    @Test
    void testExcluirNaoEncontrado() {
        Integer idInexistente = 3;

        when(gastoRepository.findById(idInexistente)).thenReturn(Optional.empty());

        assertThrows(IdNaoCadastradoException.class, () -> gastoService.excluir(idInexistente));
        verify(gastoRepository, never()).delete(any(Gasto.class));
        verify(gastoRepository).findById(idInexistente);

    }

    private Gasto criarGasto(Integer id) {
        return new Gasto(id, 3000.0, FormaPagamento.PIX, LocalDate.of(2023, 3, 28),
                "Goiânia", "Notebook", true, new Pessoa(1, null, null, null));
    }
}