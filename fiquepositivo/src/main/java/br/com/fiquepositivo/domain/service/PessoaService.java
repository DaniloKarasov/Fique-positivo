package br.com.fiquepositivo.domain.service;

import br.com.fiquepositivo.domain.exceptions.ConflitoDeDadosException;
import br.com.fiquepositivo.domain.exceptions.IdNaoCadastradoException;
import br.com.fiquepositivo.domain.model.Pessoa;
import br.com.fiquepositivo.domain.repository.PessoaRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

@Service
public class PessoaService {

    private final PessoaRepository pessoaRepository;

    public PessoaService(PessoaRepository pessoaRepository) {
        this.pessoaRepository = pessoaRepository;
    }

    public List<Pessoa> listar() {
        return pessoaRepository.findAll();
    }

    public Pessoa buscar(Integer pessoaId) {
        return pessoaRepository.findById(pessoaId).orElseThrow(
                () -> new IdNaoCadastradoException(String.format("Não existe pessoa com id %s.", pessoaId)));
    }

    public Pessoa salvar(Pessoa pessoa) {
        return pessoaRepository.save(pessoa);
    }

    public Pessoa atualizar(Integer pessoaId, Pessoa pessoa) {
        Pessoa pessoaExistente = buscar(pessoaId);
        BeanUtils.copyProperties(pessoa, pessoaExistente, "id");
        return salvar(pessoaExistente);
    }

    public Pessoa atualizarParcialmente(Integer pessoaId, Map<String, Object> dados) {
        Pessoa pessoaExistente = buscar(pessoaId);
        ObjectMapper objectMapper = new ObjectMapper();
        Pessoa pessoaDados = objectMapper.convertValue(dados, Pessoa.class);

        dados.forEach((nomeAtributo, valorAtributo) -> {
            Field field = ReflectionUtils.findField(Pessoa.class, nomeAtributo);
            field.setAccessible(true);

            Object valorConvertido = ReflectionUtils.getField(field, pessoaDados);
            ReflectionUtils.setField(field, pessoaExistente, valorConvertido);

        });
        return atualizar(pessoaId, pessoaExistente);
    }

    public void excluir(Integer pessoaId) {
        try {
            pessoaRepository.delete(buscar(pessoaId));

        } catch (DataIntegrityViolationException e) {
            throw new ConflitoDeDadosException(
                    String.format("A pessoa com o id %s não pode ser excluída porque existe gasto/s vinculado/s a ela.",
                            pessoaId));
        }
    }
}
