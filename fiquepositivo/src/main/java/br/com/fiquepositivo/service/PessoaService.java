package br.com.fiquepositivo.service;

import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import br.com.fiquepositivo.exceptions.ConflitoDeDadosException;
import br.com.fiquepositivo.exceptions.IdNaoCadastradoException;
import br.com.fiquepositivo.model.Pessoa;
import br.com.fiquepositivo.repository.PessoaRepository;

@Service
public class PessoaService {
	
	@Autowired
	private PessoaRepository pessoaRepository;
	
	public Pessoa salvar(Pessoa pessoa) {
		return pessoaRepository.save(pessoa);
	}
	
	public Pessoa atualizar(Integer pessoaId, Pessoa pessoa) {
		Optional<Pessoa> pessoaExistente = pessoaRepository.findById(pessoaId);
		if(pessoaExistente.isPresent()) {
			BeanUtils.copyProperties(pessoa, pessoaExistente.get(), "id");
			return salvar(pessoaExistente.get());
		} else {
			throw new IdNaoCadastradoException(String.format("Não existe pessoa com id %s.", pessoaId));
		}
	}
	
	public void excluir(Integer pessoaId) {
		try {
			Optional<Pessoa> pessoa = pessoaRepository.findById(pessoaId);
			if(pessoa.isPresent()) {
				pessoaRepository.delete(pessoa.get());
			} else {
				throw new IdNaoCadastradoException(String.format("Não existe pessoa com id %s.", pessoaId));
			}
			
		} catch (DataIntegrityViolationException e) {
			throw new ConflitoDeDadosException(String.format("A pessoa com o id %s não pode ser excluída porque existe gasto/s vinculado/s a ela.", pessoaId));
		}
	}
}
