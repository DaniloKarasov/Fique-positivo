package br.com.fiquepositivo.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
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