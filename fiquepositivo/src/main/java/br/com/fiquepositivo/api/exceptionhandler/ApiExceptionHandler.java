package br.com.fiquepositivo.api.exceptionhandler;

import br.com.fiquepositivo.api.dto.output.ErrorResponse;
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

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(IdNaoCadastradoException.class)
    public ResponseEntity<ErrorResponse> tratarIdNaoCadastradoException(IdNaoCadastradoException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(criarErrorResponse(HttpStatus.NOT_FOUND, e.getMessage()));
    }

    @ExceptionHandler(ConflitoDeDadosException.class)
    public ResponseEntity<ErrorResponse> tratarConflitoDeDadosException(ConflitoDeDadosException e) {

        return ResponseEntity.status(HttpStatus.CONFLICT).body(criarErrorResponse(HttpStatus.CONFLICT, e.getMessage()));
    }

    @ExceptionHandler(PessoaNaoEncontradaException.class)
    public ResponseEntity<ErrorResponse> tratarPessoaNaoEncontradaException(PessoaNaoEncontradaException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(criarErrorResponse(HttpStatus.BAD_REQUEST, e.getMessage()));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> tratarHttpMessageNotReadableException() {
        return ResponseEntity.badRequest()
                .body(criarErrorResponse(HttpStatus.BAD_REQUEST, "Erro de sintaxe no JSON enviado."));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> tratarMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        List<String> mensagens = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.toList());
        String mensagem = String.join(" ", mensagens);
        return ResponseEntity.badRequest().body(criarErrorResponse(HttpStatus.BAD_REQUEST, mensagem));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> tratarDataIntegrityViolationException(DataIntegrityViolationException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(criarErrorResponse(HttpStatus.BAD_REQUEST,
                        "Um ou mais campos obrigatórios não foram preenchidos ou os dados são inconsistentes."));
    }

    private ErrorResponse criarErrorResponse(HttpStatus status, String message) {
        return new ErrorResponse(status.value(), message, LocalDateTime.now());
    }

}