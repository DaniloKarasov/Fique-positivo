package br.com.fiquepositivo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
		pessoaRepository.deleteById(pessoaId);
	}
}
