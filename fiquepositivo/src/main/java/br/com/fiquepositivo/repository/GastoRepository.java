package br.com.fiquepositivo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.fiquepositivo.model.Gasto;

public interface GastoRepository extends JpaRepository<Gasto, Integer> {

}
