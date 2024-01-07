package br.com.fiquepositivo.model;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Gasto {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@EqualsAndHashCode.Include
	private Integer id;
	
	@Column(nullable = false)
	private Double valor;

	@Column
	private FormaPagamentoENUM formaPagamento;

	@Column
	private LocalDate data;
	
	@Column(length = 60)
	private String local;

	@Column
	private String descricao;
	
	@Column
	private Boolean necessidade;
	
	@JoinColumn(nullable = false)
	@ManyToOne
	private Pessoa pessoa;
	
	
}