package br.com.fiquepositivo.api.exceptionhandler;

public final class MensagensErro {

    private MensagensErro() {
    }

    public static final String ERRO_PESSOA_NAO_ENCONTRADA = "Não existe pessoa com id %s.";
    public static final String ERRO_CONFLITO_PESSOA =
            "A pessoa com o id %s não pode ser excluída porque existe gasto/s vinculado/s a ela.";
    public static final String ERRO_GASTO_NAO_ENCONTRADO = "Não existe gasto com id %s.";
    public static final String ERRO_JSON_MAL_FORMADO = "Erro de sintaxe no JSON enviado.";
    public static final String ERRO_CAMPOS_INVALIDOS =
            "Um ou mais campos obrigatórios não foram preenchidos ou os dados são inconsistentes.";

    public static final String VALIDACAO_NOME_PESSOA_FALTANDO = "O nome é obrigatório.";
    public static final String VALIDACAO_RENDA_NULA = "O campo renda mensal não pode ser nulo.";
    public static final String VALIDACAO_RENDA_NEGATIVA = "O campo renda mensal não pode ser negativo.";
    public static final String VALIDACAO_GASTO_VALOR_NULO = "O valor do gasto não pode ser nulo";
    public static final String VALIDACAO_GASTO_VALOR_NEGATIVO = "O valor não pode ser negativo.";
}
