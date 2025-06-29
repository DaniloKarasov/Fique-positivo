package br.com.fiquepositivo.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Pessoa {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@EqualsAndHashCode.Include
	private Integer id;

	@NotBlank(message = "O nome é obrigatório.")
	@Column(nullable = false, length = 60)
	private String nome;

	@NotNull(message = "O campo renda mensal não pode ser nulo")
	@Positive(message = "O campo renda mensal não pode ser negativo")
	@Column(nullable = false)
	private Double rendaMensal;
	
	@Column(length = 60)
	private String profissao;
	
	
}