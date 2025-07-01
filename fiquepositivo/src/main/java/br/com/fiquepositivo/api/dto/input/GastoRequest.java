package br.com.fiquepositivo.api.dto.input;

import br.com.fiquepositivo.domain.model.FormaPagamento;

import java.time.LocalDate;

public record GastoRequest(
        Double valor,
        FormaPagamento formaPagamento,
        LocalDate data,
        String local,
        String descricao,
        Boolean necessidade,
        Integer pessoaId
) {
}
