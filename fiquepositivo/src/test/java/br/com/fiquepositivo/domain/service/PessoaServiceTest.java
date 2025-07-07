package br.com.fiquepositivo.domain.service;

import br.com.fiquepositivo.domain.exceptions.IdNaoCadastradoException;
import br.com.fiquepositivo.domain.model.Pessoa;
import br.com.fiquepositivo.domain.repository.PessoaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PessoaServiceTest {

    @Mock
    PessoaRepository pessoaRepository;

    @InjectMocks
    PessoaService pessoaService;

    @DisplayName("Deve retornar um List<Pessoa>")
    @Test
    void testListar() {
        Pessoa pessoa1 = new Pessoa(1, "Fábio", 5000.0, "Analista de sistemas");
        Pessoa pessoa2 = new Pessoa(2, "Pedro", 4000.0, "Corretor");
        List<Pessoa> pessoaList = List.of(pessoa1, pessoa2);

        when(pessoaRepository.findAll()).thenReturn(pessoaList);

        List<Pessoa> result = pessoaService.listar();

        assertEquals(pessoaList, result);
    }

    @DisplayName("Deve retornar uma Pessoa")
    @Test
    void testBuscar() {
        Integer id = 1;
        Pessoa pessoa = new Pessoa(1, "Fábio", 5000.0, "Analista de sistemas");

        when(pessoaRepository.findById(id)).thenReturn(Optional.of(pessoa));

        Pessoa result = pessoaService.buscar(id);

        assertEquals(pessoa, result);
    }

    @DisplayName("Deve lançar IdNaoCadastradoException ao buscar pessoa com id inválido")
    @Test
    void testBuscarNaoEncontrado() {
        Integer idInexistente = 4;

        when(pessoaRepository.findById(idInexistente)).thenReturn(Optional.empty());
        assertThrows(IdNaoCadastradoException.class, () -> pessoaService.buscar(idInexistente));

        verify(pessoaRepository).findById(idInexistente);
    }

    @DisplayName("Deve retornar a pessoa salva")
    @Test
    void testSalvar() {
        Pessoa pessoa = new Pessoa(null, "Fábio", 5000.0, "Analista de sistemas");
        Pessoa pessoaSalva = new Pessoa(1, "Fábio", 5000.0, "Analista de sistemas");

        when(pessoaRepository.save(pessoa)).thenReturn(pessoaSalva);

        Pessoa result = pessoaService.salvar(pessoa);

        assertEquals(pessoaSalva, result);
    }

    @DisplayName("Deve retornar a pessoa atualizada")
    @Test
    void testAtualizar() {
        Integer id = 1;
        Pessoa pessoaExistente = new Pessoa(id, "Fábio", 5000.0, "Analista de sistemas");
        Pessoa novosDados = new Pessoa(null, "Fábio Augusto", 5500.0, "Engenheiro de software");
        Pessoa pessoaAtualizada = new Pessoa(id, "Fábio Augusto", 5500.0, "Engenheiro de software");

        when(pessoaRepository.findById(id)).thenReturn(Optional.of(pessoaExistente));
        when(pessoaRepository.save(pessoaExistente)).thenReturn(pessoaExistente);

        Pessoa result = pessoaService.atualizar(id, novosDados);

        assertThat(pessoaAtualizada).usingRecursiveComparison().isEqualTo(result);
    }

    @DisplayName("Deve lançar IdNaoCadastradoException ao tentar atualizar pessoa com id inexistente")
    @Test
    void testAtualizarNaoEncontrado() {
        Integer idInexistente = 3;
        Pessoa pessoa = new Pessoa(1, "Fábio", 5000.0, "Analista de sistemas");

        when(pessoaRepository.findById(idInexistente)).thenReturn(Optional.empty());
        assertThrows(IdNaoCadastradoException.class, () -> pessoaService.atualizar(idInexistente, pessoa));

        verify(pessoaRepository, never()).save(any(Pessoa.class));
    }

    @DisplayName("Deve retornar a pessoa atualizada parcialmente")
    @Test
    void testAtualizarParcialmente() {
        Integer id = 1;
        Pessoa pessoaExistente = new Pessoa(id, "Fábio", 5000.0, "Analista de sistemas");
        Map<String, Object> dados = new HashMap<>();
        dados.put("profissao", null);
        dados.put("rendaMensal", 200.0);
        Pessoa pessoaAtualizada = new Pessoa(id, "Fábio", 200.0, null);

        when(pessoaRepository.findById(id)).thenReturn(Optional.of(pessoaExistente));
        when(pessoaRepository.save(pessoaExistente)).thenReturn(pessoaExistente);

        Pessoa result = pessoaService.atualizarParcialmente(id, dados);

        assertThat(pessoaAtualizada).usingRecursiveComparison().isEqualTo(result);
    }

    @DisplayName("Deve lançar IdNaoCadastradoException ao atualizar parcialmente com id inexistente")
    @Test
    void testAtualizarParcialmenteNaoEncontrado() {
        Integer idInexistente = 2;
        Map<String, Object> dados = new HashMap<>();
        dados.put("profissao", null);
        dados.put("rendaMensal", 200.0);

        when(pessoaRepository.findById(idInexistente)).thenReturn(Optional.empty());

        assertThrows(IdNaoCadastradoException.class, () -> pessoaService.atualizarParcialmente(idInexistente, dados));
        verify(pessoaRepository, times(1)).findById(idInexistente);
        verify(pessoaRepository, never()).save(any(Pessoa.class));
    }

    @DisplayName("Deve excluir uma pessoa e não retornar nada")
    @Test
    void testExcluir() {
        Integer id = 1;
        Pessoa pessoa = new Pessoa(id, "Fábio", 5000.0, "Analista de sistemas");

        when(pessoaRepository.findById(id)).thenReturn(Optional.of(pessoa));
        doNothing().when(pessoaRepository).delete(pessoa);

        pessoaService.excluir(id);

        verify(pessoaRepository).findById(id);
        verify(pessoaRepository).delete(pessoa);

    }

    @DisplayName("Deve lançar IdNaoCadastradoException ao excluir uma pessoa com id inexistente")
    @Test
    void testExcluirNaoEncontrado() {
        Integer idInexistente = 4;

        when(pessoaRepository.findById(idInexistente)).thenReturn(Optional.empty());

        assertThrows(IdNaoCadastradoException.class, () -> pessoaService.excluir(idInexistente));
        verify(pessoaRepository, never()).delete(any());
    }
}