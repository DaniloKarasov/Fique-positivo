package br.com.fiquepositivo.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
public class Gasto {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@EqualsAndHashCode.Include
	private Integer idGasto;
	
	@Column(nullable = false)
	private Double valor;

	@Column
	private String formaPagamento;

	@Column
	private String data;
	
	@Column
	private String local;

	@Column
	private String descricao;
	
	@Column
	private Boolean necessidade;
	
	
}
