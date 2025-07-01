package br.com.fiquepositivo.api.dto.input;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record PessoaRequest(

        @NotBlank(message = "O nome é obrigatório.")
        String nome,

        @NotNull(message = "O campo renda mensal não pode ser nulo.")
        @Positive(message = "O campo renda mensal não pode ser negativo.")
        Double rendaMensal,

        String profissao
) {
}
