package br.com.fiquepositivo.api.controller;

import br.com.fiquepositivo.api.dto.input.PessoaRequest;
import br.com.fiquepositivo.api.dto.output.PessoaDTO;
import br.com.fiquepositivo.api.mapper.PessoaMapper;
import br.com.fiquepositivo.domain.model.Pessoa;
import br.com.fiquepositivo.domain.service.PessoaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/pessoas")
public class PessoaController {

    private final PessoaService pessoaService;

    private final PessoaMapper pessoaMapper;

    public PessoaController(PessoaService pessoaService, PessoaMapper pessoaMapper) {
        this.pessoaService = pessoaService;
        this.pessoaMapper = pessoaMapper;
    }

    @GetMapping
    public List<PessoaDTO> listar() {
        return pessoaMapper.toDtoList(pessoaService.listar());
    }

    @GetMapping("/{pessoaId}")
    public PessoaDTO buscar(@PathVariable Integer pessoaId) {
        return pessoaMapper.toDto(pessoaService.buscar(pessoaId));
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public PessoaDTO adicionar(@RequestBody @Valid PessoaRequest pessoaRequest) {
        return pessoaMapper.toDto(pessoaService.salvar(pessoaMapper.toEntity(pessoaRequest)));
    }

    @PutMapping("/{pessoaId}")
    public PessoaDTO atualizar(@PathVariable Integer pessoaId, @RequestBody @Valid PessoaRequest pessoaRequest) {
        return pessoaMapper.toDto(pessoaService.atualizar(pessoaId, pessoaMapper.toEntity(pessoaRequest)));
    }

    @PatchMapping("/{pessoaId}")
    public PessoaDTO atualizarParcialmente(@PathVariable Integer pessoaId, @RequestBody Map<String, Object> dados) {
        return pessoaMapper.toDto(pessoaService.atualizarParcialmente(pessoaId, dados));
    }

    @DeleteMapping("/{pessoaId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable Integer pessoaId) {
        pessoaService.excluir(pessoaId);
    }
}
