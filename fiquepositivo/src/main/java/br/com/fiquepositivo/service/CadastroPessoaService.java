package br.com.fiquepositivo.service;

import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import br.com.fiquepositivo.exceptions.ConflitoDeDadosException;
import br.com.fiquepositivo.exceptions.IdNaoCadastradoException;
import br.com.fiquepositivo.model.Pessoa;
import br.com.fiquepositivo.repository.PessoaRepository;

@Service
public class CadastroPessoaService {
	
	@Autowired
	private PessoaRepository pessoaRepository;
	
	public Pessoa salvar(Pessoa pessoa) {
		return pessoaRepository.save(pessoa);
	}
	
	public void excluir(Integer pessoaId) {
		try {
			Optional<Pessoa> pessoa = pessoaRepository.findById(pessoaId);
			pessoaRepository.delete(pessoa.get());
			
		} catch (DataIntegrityViolationException e) {
			throw new ConflitoDeDadosException(String.format("A pessoa com o id = %s não pode ser excluída porque existe gasto/s vinculado/s a ela.", pessoaId));
		} catch (NoSuchElementException e) {
			throw new IdNaoCadastradoException(String.format("A pessoa com o id = %s não existe", pessoaId));
		}
	}
}
