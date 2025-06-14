package br.com.fiquepositivo.controller;

import br.com.fiquepositivo.exceptions.ConflitoDeDadosException;
import br.com.fiquepositivo.exceptions.IdNaoCadastradoException;
import br.com.fiquepositivo.model.Pessoa;
import br.com.fiquepositivo.service.PessoaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/pessoas")
public class PessoaController {

    private final PessoaService pessoaService;

    public PessoaController(PessoaService pessoaService) {
        this.pessoaService = pessoaService;
    }

    @GetMapping
    public List<Pessoa> listar() {
        return pessoaService.listar();
    }

    @GetMapping("/{pessoaId}")
    public ResponseEntity<Pessoa> buscar(@PathVariable Integer pessoaId) {
        return pessoaService.buscar(pessoaId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Pessoa adicionar(@RequestBody Pessoa pessoa) {
        return pessoaService.salvar(pessoa);
    }

    @PutMapping("/{pessoaId}")
    public ResponseEntity<?> atualizar(@PathVariable Integer pessoaId, @RequestBody Pessoa pessoa) {
        try {
            pessoa = pessoaService.atualizar(pessoaId, pessoa);
            return ResponseEntity.ok(pessoa);
        } catch (IdNaoCadastradoException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PatchMapping("/{pessoaId}")
    public ResponseEntity<?> atualizarParcialmente(@PathVariable Integer pessoaId,
                                                   @RequestBody Map<String, Object> dados) {
        try {
            Pessoa pessoa = pessoaService.atualizarParcialmente(pessoaId, dados);
            return ResponseEntity.ok(pessoa);
        } catch (IdNaoCadastradoException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
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
