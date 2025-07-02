package br.com.fiquepositivo.api.mapper;

import br.com.fiquepositivo.api.dto.input.PessoaRequest;
import br.com.fiquepositivo.api.dto.output.PessoaDTO;
import br.com.fiquepositivo.domain.model.Pessoa;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class PessoaMapperTest {

    @InjectMocks
    PessoaMapper pessoaMapper;

    @Test
    void testToEntity() {
        PessoaRequest pessoaRequest = new PessoaRequest("Renan", 3000.0, "pedreiro");

        Pessoa pessoa = pessoaMapper.toEntity(pessoaRequest);

        assertNull(pessoa.getId());
        assertEquals(pessoaRequest.nome(), pessoa.getNome());
        assertEquals(pessoaRequest.rendaMensal(), pessoa.getRendaMensal());
        assertEquals(pessoaRequest.profissao(), pessoa.getProfissao());

    }

    @Test
    void testToDto() {
        Pessoa pessoa = new Pessoa(2, "Carlos", 2500.0, "Cuidador");

        PessoaDTO pessoaDTO = pessoaMapper.toDto(pessoa);

        assertEquals(pessoa.getId(), pessoaDTO.id());
        assertEquals(pessoa.getNome(), pessoaDTO.nome());
        assertEquals(pessoa.getRendaMensal(), pessoaDTO.rendaMensal());
        assertEquals(pessoa.getProfissao(), pessoaDTO.profissao());
    }

    @Test
    void testToDtoList() {
        Pessoa pessoa1 = new Pessoa(1, "André", 5000.0, "Jogador");
        Pessoa pessoa2 = new Pessoa(2, "Carlos", 2500.0, "Cuidador");
        List<Pessoa> pessoaList = List.of(pessoa1, pessoa2);

        List<PessoaDTO> pessoaDTOList = pessoaMapper.toDtoList(pessoaList);

        for (int i = 0; i < pessoaList.size(); i++) {
            assertEquals(pessoaList.get(i).getId(), pessoaDTOList.get(i).id());
            assertEquals(pessoaList.get(i).getNome(), pessoaDTOList.get(i).nome());
            assertEquals(pessoaList.get(i).getRendaMensal(), pessoaDTOList.get(i).rendaMensal());
            assertEquals(pessoaList.get(i).getProfissao(), pessoaDTOList.get(i).profissao());
        }
    }
}