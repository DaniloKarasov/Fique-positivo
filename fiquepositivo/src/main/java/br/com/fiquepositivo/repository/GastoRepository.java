package br.com.fiquepositivo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.fiquepositivo.model.Gasto;

@Repository
public interface GastoRepository extends JpaRepository<Gasto, Integer> {

}
