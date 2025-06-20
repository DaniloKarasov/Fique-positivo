package br.com.fiquepositivo.api.controller;

import br.com.fiquepositivo.domain.exceptions.IdNaoCadastradoException;
import br.com.fiquepositivo.domain.model.Gasto;
import br.com.fiquepositivo.domain.service.GastoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/gastos")
public class GastoController {

    private final GastoService gastoService;

    public GastoController(GastoService gastoService) {
        this.gastoService = gastoService;
    }

    @GetMapping
    public List<Gasto> listar() {
        return gastoService.listar();
    }

    @GetMapping("/{gastoId}")
    public ResponseEntity<Gasto> buscar(@PathVariable Integer gastoId) {
        return gastoService.buscar(gastoId);
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
