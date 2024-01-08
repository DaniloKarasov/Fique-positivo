package br.com.fiquepositivo.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.fiquepositivo.exceptions.ChaveEstrangeiraInvalidaException;
import br.com.fiquepositivo.exceptions.IdNaoCadastradoException;
import br.com.fiquepositivo.model.Gasto;
import br.com.fiquepositivo.repository.GastoRepository;
import br.com.fiquepositivo.service.GastoService;

@RestController
@RequestMapping("/gastos")
public class GastoController {
	
	@Autowired
	private GastoRepository gastoRepository;
	
	@Autowired
	private GastoService gastoService;
	
	@GetMapping
	public List<Gasto> listar() {
		return gastoRepository.findAll();
	}
	
	@GetMapping("/{gastoId}")
	public ResponseEntity<Gasto> buscar(@PathVariable Integer gastoId) {
		Optional<Gasto> gasto = gastoRepository.findById(gastoId);
		if(gasto.isPresent()) {
			return ResponseEntity.ok(gasto.get());
		} else {
			return ResponseEntity.notFound().build();
		}
	}
	
	@PostMapping
	public ResponseEntity<?> adicionar(@RequestBody Gasto gasto) {
		try {
			gasto = gastoService.salvar(gasto);
			return ResponseEntity.status(HttpStatus.CREATED).body(gasto);
		} catch (IdNaoCadastradoException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	@PutMapping("/{gastoId}")
	public ResponseEntity<?> atualizar(@PathVariable Integer gastoId, @RequestBody Gasto gasto) {
		try {
			gasto = gastoService.atualizar(gastoId, gasto);
			return ResponseEntity.ok(gasto);
		} catch (IdNaoCadastradoException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}
	}
	
	@DeleteMapping("/{gastoId}")
	public ResponseEntity<?> excluir(@PathVariable Integer gastoId) {
		try {
			gastoService.excluir(gastoId);
			return ResponseEntity.noContent().build();
		} catch (IdNaoCadastradoException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}
	}
}
