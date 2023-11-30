package br.com.fiquepositivo.model;

import java.time.LocalDate;

import lombok.Data;

@Data
public class Gasto {
	private LocalDate data;
	private String descricao;
	private Double valor;
	private Boolean necessidade;
	private String local;
	private Enum formaPagamento;
}
