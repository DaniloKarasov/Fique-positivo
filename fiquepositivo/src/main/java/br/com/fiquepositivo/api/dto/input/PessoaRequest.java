package br.com.fiquepositivo.api.dto.input;

import br.com.fiquepositivo.api.exceptionhandler.MensagensErro;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record PessoaRequest(

        @NotBlank(message = MensagensErro.VALIDACAO_NOME_PESSOA_FALTANDO)
        String nome,

        @NotNull(message = MensagensErro.VALIDACAO_RENDA_NULA)
        @Positive(message = MensagensErro.VALIDACAO_RENDA_NEGATIVA)
        Double rendaMensal,

        String profissao
) {
}
