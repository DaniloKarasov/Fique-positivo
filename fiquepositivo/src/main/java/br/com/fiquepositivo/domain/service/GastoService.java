package br.com.fiquepositivo.domain.service;

import br.com.fiquepositivo.domain.exceptions.IdNaoCadastradoException;
import br.com.fiquepositivo.domain.exceptions.PessoaNaoEncontradaException;
import br.com.fiquepositivo.domain.model.Gasto;
import br.com.fiquepositivo.domain.repository.GastoRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GastoService {

    private GastoRepository gastoRepository;
    private PessoaService pessoaService;

    public GastoService(GastoRepository gastoRepository, PessoaService pessoaService) {
        this.gastoRepository = gastoRepository;
        this.pessoaService = pessoaService;
    }

    public List<Gasto> listar() {
        return gastoRepository.findAll();
    }

    public Gasto buscar(Integer gastoId) {
        return gastoRepository.findById(gastoId)
                .orElseThrow(() -> new IdNaoCadastradoException(String.format("Não existe gasto com id %s.", gastoId)));
    }

    public Gasto salvar(Gasto gasto) {
        Integer pessoaId = gasto.getPessoa().getId();
        try {
            pessoaService.buscar(pessoaId);
        } catch (IdNaoCadastradoException e) {
            throw new PessoaNaoEncontradaException(String.format("Não foi encontrada pessoa com id %s.", pessoaId));
        }
        return gastoRepository.save(gasto);
    }

    public Gasto atualizar(Integer gastoId, Gasto gastoAtualizado) {
        Gasto gastoExistente = buscar(gastoId);
        BeanUtils.copyProperties(gastoAtualizado, gastoExistente, "id");
        return salvar(gastoExistente);
    }

    public void excluir(Integer gastoId) {
        gastoRepository.delete(buscar(gastoId));
    }
}
