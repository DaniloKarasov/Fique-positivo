package br.com.fiquepositivo.service;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.fiquepositivo.exceptions.ConflitoDeDadosException;
import br.com.fiquepositivo.exceptions.IdNaoCadastradoException;
import br.com.fiquepositivo.model.Pessoa;
import br.com.fiquepositivo.repository.PessoaRepository;
import org.springframework.web.bind.annotation.PathVariable;

@Service
public class PessoaService {
	
	@Autowired
	private PessoaRepository pessoaRepository;

	public List<Pessoa> listar() {
		return pessoaRepository.findAll();
	}

	public ResponseEntity<Pessoa> buscar(Integer pessoaId) {
		Optional<Pessoa> pessoa = pessoaRepository.findById(pessoaId);
		if(pessoa.isPresent()) {
			return ResponseEntity.ok(pessoa.get());
		} else {
			return ResponseEntity.notFound().build();
		}
	}
	
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
	
	public Pessoa atualizarParcialmente(Integer pessoaId, Map<String, Object> dados) {
		Optional<Pessoa> pessoaExistente = pessoaRepository.findById(pessoaId);
		ObjectMapper objectMapper = new ObjectMapper();
		Pessoa pessoaDados = objectMapper.convertValue(dados, Pessoa.class);
		if(pessoaExistente.isPresent()) {
			dados.forEach((nomeAtributo, valorAtributo) -> {
				Field field = ReflectionUtils.findField(Pessoa.class, nomeAtributo);
				field.setAccessible(true);
				
				Object valorConvertido = ReflectionUtils.getField(field, pessoaDados);
				ReflectionUtils.setField(field, pessoaExistente.get(), valorConvertido);
		
			});
			return atualizar(pessoaId, pessoaExistente.get());
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
