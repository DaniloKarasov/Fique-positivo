package br.com.fiquepositivo.domain.service;

import br.com.fiquepositivo.domain.exceptions.IdNaoCadastradoException;
import br.com.fiquepositivo.domain.model.Pessoa;
import br.com.fiquepositivo.domain.repository.PessoaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PessoaServiceTest {

    @Mock
    PessoaRepository pessoaRepository;

    @InjectMocks
    PessoaService pessoaService;

    @Test
    void testListar() {
        Pessoa pessoa1 = new Pessoa(1, "Fábio", 5000.0, "Analista de sistemas");
        Pessoa pessoa2 = new Pessoa(2, "Pedro", 4000.0, "Corretor");
        List<Pessoa> pessoaList = List.of(pessoa1, pessoa2);

        when(pessoaRepository.findAll()).thenReturn(pessoaList);

        List<Pessoa> result = pessoaService.listar();

        assertEquals(pessoaList, result);
    }

    @Test
    void testBuscar() {
        Integer id = 1;
        Pessoa pessoa = new Pessoa(1, "Fábio", 5000.0, "Analista de sistemas");

        when(pessoaRepository.findById(id)).thenReturn(Optional.of(pessoa));

        Pessoa result = pessoaService.buscar(id);

        assertEquals(pessoa, result);
    }

    @Test
    void testSalvar() {
        Pessoa pessoa = new Pessoa(null, "Fábio", 5000.0, "Analista de sistemas");
        Pessoa pessoaSalva = new Pessoa(1, "Fábio", 5000.0, "Analista de sistemas");

        when(pessoaRepository.save(pessoa)).thenReturn(pessoaSalva);

        Pessoa result = pessoaService.salvar(pessoa);

        assertEquals(pessoaSalva, result);
    }

    @Test
    void atualizar() {
        Integer id = 1;
        Pessoa pessoaExistente = new Pessoa(id, "Fábio", 5000.0, "Analista de sistemas");
        Pessoa novosDados = new Pessoa(null, "Fábio Augusto", 5500.0, "Engenheiro de software");
        Pessoa pessoaAtualizada = new Pessoa(id, "Fábio Augusto", 5500.0, "Engenheiro de software");

        when(pessoaRepository.findById(id)).thenReturn(Optional.of(pessoaExistente));
        when(pessoaRepository.save(pessoaExistente)).thenReturn(pessoaExistente);

        Pessoa result = pessoaService.atualizar(id, novosDados);

        assertEquals(pessoaAtualizada, result);
    }

    @Test
    void atualizarParcialmente() {
        Integer id = 1;
        Pessoa pessoaExistente = new Pessoa(id, "Fábio", 5000.0, "Analista de sistemas");
        Map<String, Object> dados = new HashMap<>();
        dados.put("profissao", null);
        dados.put("rendaMensal", 200.0);
        Pessoa pessoaAtualizada = new Pessoa(id, "Fábio", 200.0, null);

        when(pessoaRepository.findById(id)).thenReturn(Optional.of(pessoaExistente));
        when(pessoaRepository.save(pessoaExistente)).thenReturn(pessoaExistente);

        Pessoa result = pessoaService.atualizarParcialmente(id, dados);

        assertEquals(pessoaAtualizada, result);
    }

    @Test
    void excluir() {
        Integer id = 1;
        Pessoa pessoa = new Pessoa(id, "Fábio", 5000.0, "Analista de sistemas");

        when(pessoaRepository.findById(id)).thenReturn(Optional.of(pessoa));
        doNothing().when(pessoaRepository).delete(pessoa);

        pessoaService.excluir(id);

        verify(pessoaRepository).findById(id);
        verify(pessoaRepository).delete(pessoa);

    }

    @Test
    void testExcluirIdInvalido() {
        Integer id = 4;

        when(pessoaRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(IdNaoCadastradoException.class, () -> pessoaService.excluir(id));
        verify(pessoaRepository, never()).delete(any());
    }
}