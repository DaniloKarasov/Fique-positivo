package br.com.fiquepositivo.service;

import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import br.com.fiquepositivo.exceptions.ChaveEstrangeiraInvalidaException;
import br.com.fiquepositivo.exceptions.IdNaoCadastradoException;
import br.com.fiquepositivo.model.Gasto;
import br.com.fiquepositivo.model.Pessoa;
import br.com.fiquepositivo.repository.GastoRepository;
import br.com.fiquepositivo.repository.PessoaRepository;

@Service
public class GastoService {
	
	@Autowired
	private GastoRepository gastoRepository;
	
	@Autowired
	private PessoaRepository pessoaRepository;
	
	public Gasto salvar(Gasto gasto) {
		try {
			return gastoRepository.save(gasto);
		} catch (DataIntegrityViolationException e) {
			throw new ChaveEstrangeiraInvalidaException("Não existe pessoa com o id informado.");
		}
	}
	
	public Gasto atualizar(Integer gastoId, Gasto gastoAtualizado) {
		Optional<Gasto> gastoExistente = gastoRepository.findById(gastoId);
		if(gastoExistente.isPresent()) {
			Integer pessoaId = gastoAtualizado.getPessoa().getId();
			Optional<Pessoa> pessoa = pessoaRepository.findById(pessoaId);
			if(pessoa.isEmpty()) {
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
		if(gasto.isPresent()) {
			gastoRepository.delete(gasto.get());
		} else {
			throw new IdNaoCadastradoException(String.format("Não existe gasto com id %s.", gastoId));
		}
	}
}
