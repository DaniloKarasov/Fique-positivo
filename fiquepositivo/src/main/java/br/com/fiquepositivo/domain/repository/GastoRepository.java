package br.com.fiquepositivo.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.fiquepositivo.domain.model.Gasto;

@Repository
public interface GastoRepository extends JpaRepository<Gasto, Integer> {

}
