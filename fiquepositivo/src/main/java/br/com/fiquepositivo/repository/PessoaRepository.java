package br.com.fiquepositivo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.fiquepositivo.model.Pessoa;

@Repository
public interface PessoaRepository extends JpaRepository<Pessoa, Integer> {

}
