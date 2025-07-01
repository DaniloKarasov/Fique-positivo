package br.com.fiquepositivo.api.controller;

import br.com.fiquepositivo.api.dto.input.GastoRequest;
import br.com.fiquepositivo.api.dto.output.GastoDTO;
import br.com.fiquepositivo.api.mapper.GastoMapper;
import br.com.fiquepositivo.domain.service.GastoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/gastos")
public class GastoController {

    private final GastoService gastoService;

    private final GastoMapper gastoMapper;

    public GastoController(GastoService gastoService, GastoMapper gastoMapper) {
        this.gastoService = gastoService;
        this.gastoMapper = gastoMapper;
    }

    @GetMapping
    public List<GastoDTO> listar() {
        return gastoMapper.toDtoList(gastoService.listar());
    }

    @GetMapping("/{gastoId}")
    public GastoDTO buscar(@PathVariable Integer gastoId) {
        return gastoMapper.toDto(gastoService.buscar(gastoId));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public GastoDTO adicionar(@RequestBody @Valid GastoRequest gastoRequest) {
        return gastoMapper.toDto(gastoService.salvar(gastoMapper.toEntity(gastoRequest)));
    }

    @PutMapping("/{gastoId}")
    public GastoDTO atualizar(@PathVariable Integer gastoId, @RequestBody @Valid GastoRequest gastoRequest) {
        return gastoMapper.toDto(gastoService.atualizar(gastoId, gastoMapper.toEntity(gastoRequest)));
    }

    @DeleteMapping("/{gastoId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable Integer gastoId) {
        gastoService.excluir(gastoId);
    }
}
