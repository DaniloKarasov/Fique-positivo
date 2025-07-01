package br.com.fiquepositivo.api.mapper;

import br.com.fiquepositivo.api.dto.input.PessoaRequest;
import br.com.fiquepositivo.api.dto.output.PessoaDTO;
import br.com.fiquepositivo.domain.model.Pessoa;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class PessoaMapper {
    public Pessoa toEntity(PessoaRequest pessoaDto) {
        return new Pessoa(null,
                pessoaDto.nome(),
                pessoaDto.rendaMensal(),
                pessoaDto.profissao());
    }

    public PessoaDTO toDto(Pessoa pessoa) {
        return new PessoaDTO(
                pessoa.getId(),
                pessoa.getNome(),
                pessoa.getRendaMensal(),
                pessoa.getProfissao());
    }

    public List<PessoaDTO> toDtoList(List<Pessoa> pessoaList) {
        List<PessoaDTO> pessoaDTOList = new ArrayList<>();
        for (Pessoa pessoa : pessoaList) {
            PessoaDTO pessoaDto = toDto(pessoa);
            pessoaDTOList.add(pessoaDto);
        }
        return pessoaDTOList;
    }
}
