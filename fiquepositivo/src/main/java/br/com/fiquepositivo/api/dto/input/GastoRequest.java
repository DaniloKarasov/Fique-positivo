package br.com.fiquepositivo.api.dto.input;

import br.com.fiquepositivo.api.exceptionhandler.MensagensErro;
import br.com.fiquepositivo.domain.model.FormaPagamento;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;

public record GastoRequest(
        @NotNull(message = MensagensErro.VALIDACAO_GASTO_VALOR_NULO)
        @Positive(message = MensagensErro.VALIDACAO_GASTO_VALOR_NEGATIVO)
        Double valor,
        FormaPagamento formaPagamento,
        LocalDate data,
        String local,
        String descricao,
        Boolean necessidade,
        Integer pessoaId
) {
}
