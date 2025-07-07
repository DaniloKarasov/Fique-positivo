package br.com.fiquepositivo.api.mapper;

import br.com.fiquepositivo.api.dto.input.GastoRequest;
import br.com.fiquepositivo.api.dto.output.GastoDTO;
import br.com.fiquepositivo.domain.model.Gasto;
import br.com.fiquepositivo.domain.model.Pessoa;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class GastoMapper {
    public Gasto toEntity(GastoRequest gastoRequest) {
        Pessoa pessoa = new Pessoa();
        pessoa.setId(gastoRequest.pessoaId());

        return new Gasto(null,
                gastoRequest.valor(),
                gastoRequest.formaPagamento(),
                gastoRequest.data(),
                gastoRequest.local(),
                gastoRequest.descricao(),
                gastoRequest.necessidade(),
                pessoa);
    }

    public GastoDTO toDto(Gasto gasto) {
        return new GastoDTO(
                gasto.getId(),
                gasto.getValor(),
                gasto.getFormaPagamento(),
                gasto.getData(),
                gasto.getLocal(),
                gasto.getDescricao(),
                gasto.getNecessidade(),
                gasto.getPessoa().getId()
        );
    }

    public List<GastoDTO> toDtoList(List<Gasto> gastoList) {
        List<GastoDTO> gastoDtoList = new ArrayList<>();
        for (Gasto gasto : gastoList) {
            GastoDTO gastoDto = toDto(gasto);
            gastoDtoList.add(gastoDto);
        }
        return gastoDtoList;
    }
}
