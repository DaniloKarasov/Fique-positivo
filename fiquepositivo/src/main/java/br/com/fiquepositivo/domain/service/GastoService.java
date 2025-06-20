package br.com.fiquepositivo.domain.service;

import br.com.fiquepositivo.domain.exceptions.IdNaoCadastradoException;
import br.com.fiquepositivo.domain.model.Gasto;
import br.com.fiquepositivo.domain.model.Pessoa;
import br.com.fiquepositivo.domain.repository.GastoRepository;
import br.com.fiquepositivo.domain.repository.PessoaRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GastoService {

    private GastoRepository gastoRepository;
    private PessoaRepository pessoaRepository;

    public GastoService(GastoRepository gastoRepository, PessoaRepository pessoaRepository) {
        this.gastoRepository = gastoRepository;
        this.pessoaRepository = pessoaRepository;
    }

    public List<Gasto> listar() {
        return gastoRepository.findAll();
    }

    public ResponseEntity<Gasto> buscar(Integer gastoId) {
        Optional<Gasto> gasto = gastoRepository.findById(gastoId);
        if (gasto.isPresent()) {
            return ResponseEntity.ok(gasto.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    public Gasto salvar(Gasto gasto) {
        Integer pessoaId = gasto.getPessoa().getId();
        Optional<Pessoa> pessoa = pessoaRepository.findById(pessoaId);
        if (pessoa.isEmpty()) {
            throw new IdNaoCadastradoException(String.format("Não existe pessoa com id %s", pessoaId));
        }
        return gastoRepository.save(gasto);

    }

    public Gasto atualizar(Integer gastoId, Gasto gastoAtualizado) {
        Optional<Gasto> gastoExistente = gastoRepository.findById(gastoId);
        if (gastoExistente.isPresent()) {
            Integer pessoaId = gastoAtualizado.getPessoa().getId();
            Optional<Pessoa> pessoa = pessoaRepository.findById(pessoaId);
            if (pessoa.isEmpty()) {
                throw new IdNaoCadastradoException(String.format("Não existe pessoa com id %s", pessoaId));
            }
            BeanUtils.copyProperties(gastoAtualizado, gastoExistente.get(), "id");
            return salvar(gastoExistente.get());
        } else {
            throw new IdNaoCadastradoException(String.format("Não existe gasto com id %s.", gastoId));
        }
    }

    public void excluir(Integer gastoId) {
        Optional<Gasto> gasto = gastoRepository.findById(gastoId);
        if (gasto.isPresent()) {
            gastoRepository.delete(gasto.get());
        } else {
            throw new IdNaoCadastradoException(String.format("Não existe gasto com id %s.", gastoId));
        }
    }
}
