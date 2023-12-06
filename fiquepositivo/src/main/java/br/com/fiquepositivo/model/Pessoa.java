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
public class Pessoa {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@EqualsAndHashCode.Include
	private Integer idPessoa;
	
	@Column(nullable = false)
	private String nome;
	
	@Column(nullable = false)
	private Double RendaMensal;
	
	@Column
	private String profissao;
	
	
}
