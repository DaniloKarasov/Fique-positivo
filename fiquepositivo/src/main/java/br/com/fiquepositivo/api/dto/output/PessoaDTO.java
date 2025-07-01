package br.com.fiquepositivo.api.dto.output;

public record PessoaDTO(
        Integer id,

        String nome,

        Double rendaMensal,

        String profissao
) {
}
