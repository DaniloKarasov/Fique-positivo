package br.com.fiquepositivo.api.controller;

import br.com.fiquepositivo.domain.model.Gasto;
import br.com.fiquepositivo.domain.service.GastoService;
import org.springframework.http.HttpStatus;
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
    public Gasto buscar(@PathVariable Integer gastoId) {
        return gastoService.buscar(gastoId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Gasto adicionar(@RequestBody Gasto gasto) {
        return gastoService.salvar(gasto);
    }

    @PutMapping("/{gastoId}")
    public Gasto atualizar(@PathVariable Integer gastoId, @RequestBody Gasto gasto) {
        return gastoService.atualizar(gastoId, gasto);
    }

    @DeleteMapping("/{gastoId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable Integer gastoId) {
        gastoService.excluir(gastoId);
    }
}
