package br.com.fiquepositivo.domain.service;

import br.com.fiquepositivo.domain.model.Pessoa;
import br.com.fiquepositivo.domain.repository.PessoaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

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

        assertEquals(pessoaList.size(), result.size());
        assertEquals(pessoaList.get(0).getId(), result.get(0).getId());
        assertEquals(pessoaList.get(1).getNome(), result.get(1).getNome());
    }

    @Test
    void buscar() {
    }

    @Test
    void salvar() {
    }

    @Test
    void atualizar() {
    }

    @Test
    void atualizarParcialmente() {
    }

    @Test
    void excluir() {
    }
}