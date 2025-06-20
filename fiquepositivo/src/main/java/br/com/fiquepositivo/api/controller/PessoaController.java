package br.com.fiquepositivo.api.controller;

import br.com.fiquepositivo.domain.model.Pessoa;
import br.com.fiquepositivo.domain.service.PessoaService;
import org.springframework.http.HttpStatus;
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
    public Pessoa buscar(@PathVariable Integer pessoaId) {
        return pessoaService.buscar(pessoaId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Pessoa adicionar(@RequestBody Pessoa pessoa) {
        return pessoaService.salvar(pessoa);
    }

    @PutMapping("/{pessoaId}")
    public Pessoa atualizar(@PathVariable Integer pessoaId, @RequestBody Pessoa pessoa) {
        pessoa = pessoaService.atualizar(pessoaId, pessoa);
        return pessoa;
    }

    @PatchMapping("/{pessoaId}")
    public Pessoa atualizarParcialmente(@PathVariable Integer pessoaId,
                                        @RequestBody Map<String, Object> dados) {
        Pessoa pessoa = pessoaService.atualizarParcialmente(pessoaId, dados);
        return pessoa;
    }

    @DeleteMapping("/{pessoaId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable Integer pessoaId) {
        pessoaService.excluir(pessoaId);
    }
}
