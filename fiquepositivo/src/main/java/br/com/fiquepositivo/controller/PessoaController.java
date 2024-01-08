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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.fiquepositivo.exceptions.ConflitoDeDadosException;
import br.com.fiquepositivo.exceptions.IdNaoCadastradoException;
import br.com.fiquepositivo.model.Pessoa;
import br.com.fiquepositivo.repository.PessoaRepository;
import br.com.fiquepositivo.service.PessoaService;

@RestController
@RequestMapping("/pessoas")
public class PessoaController {
	
	@Autowired
	private PessoaRepository pessoaRepository;
	
	@Autowired
	private PessoaService pessoaService;
	
	@GetMapping
	public List<Pessoa> listar() {
		return pessoaRepository.findAll();
	}
	
	@GetMapping("/{pessoaId}")
	public ResponseEntity<Pessoa> buscar(@PathVariable Integer pessoaId) {
		Optional<Pessoa> pessoa = pessoaRepository.findById(pessoaId);
		if(pessoa.isPresent()) {
			return ResponseEntity.ok(pessoa.get());
		} else {
			return ResponseEntity.notFound().build();
		}
	}
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Pessoa adicionar(@RequestBody Pessoa pessoa) {
		return pessoaService.salvar(pessoa);
	}
	
	@DeleteMapping("/{pessoaId}")
	public ResponseEntity<String> excluir(@PathVariable Integer pessoaId) {
		try {
			pessoaService.excluir(pessoaId);
			return ResponseEntity.noContent().build();
		} catch (ConflitoDeDadosException e) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
		} catch (IdNaoCadastradoException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}
	}
}
