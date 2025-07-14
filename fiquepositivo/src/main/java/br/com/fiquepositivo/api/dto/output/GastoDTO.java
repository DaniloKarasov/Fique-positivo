package br.com.fiquepositivo.api.dto.output;

import br.com.fiquepositivo.domain.model.FormaPagamento;

import java.time.LocalDate;

public record GastoDTO(
        Integer id,
        Double valor,
        FormaPagamento formaPagamento,
        LocalDate data,
        String local,
        String descricao,
        Boolean necessidade,
        Integer pessoaId
) {
}
