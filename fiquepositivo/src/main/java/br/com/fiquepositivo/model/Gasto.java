package br.com.fiquepositivo.model;

import lombok.Data;

@Data
public class Gasto {
	private String data;
	private String descricao;
	private Double valor;
	private Boolean necessidade;
	private String local;
	private String formaPagamento;
}
