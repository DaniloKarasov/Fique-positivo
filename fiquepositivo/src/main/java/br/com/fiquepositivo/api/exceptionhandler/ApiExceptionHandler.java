package br.com.fiquepositivo.api.exceptionhandler;

import br.com.fiquepositivo.domain.exceptions.ConflitoDeDadosException;
import br.com.fiquepositivo.domain.exceptions.IdNaoCadastradoException;
import br.com.fiquepositivo.domain.exceptions.PessoaNaoEncontradaException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(IdNaoCadastradoException.class)
    public ResponseEntity<String> tratarIdNaoCadastradoException(IdNaoCadastradoException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    @ExceptionHandler(ConflitoDeDadosException.class)
    public ResponseEntity<String> tratarConflitoDeDadosException(ConflitoDeDadosException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
    }

    @ExceptionHandler(PessoaNaoEncontradaException.class)
    public ResponseEntity<String> tratarPessoaNaoEncontradaException(PessoaNaoEncontradaException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<String> tratarHttpMessageNotReadableException() {
        return ResponseEntity.badRequest().body("Erro de sintaxe no JSON enviado.");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<String>> tratarMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        List<String> mensagens = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.toList());
        return ResponseEntity.badRequest().body(mensagens);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<String> tratarDataIntegrityViolationException(DataIntegrityViolationException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Um ou mais campos obrigatórios não foram preenchidos ou os dados são inconsistentes.");
    }

}