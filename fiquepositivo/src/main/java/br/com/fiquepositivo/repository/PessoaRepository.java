package br.com.fiquepositivo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.fiquepositivo.model.Pessoa;

public interface PessoaRepository extends JpaRepository<Pessoa, Integer> {

}
