package br.com.fiquepositivo.exceptions;

public class IdNaoCadastradoException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public IdNaoCadastradoException(String mensagem) {
		super(mensagem);
	}
}
