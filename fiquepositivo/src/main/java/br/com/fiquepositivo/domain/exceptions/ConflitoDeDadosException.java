package br.com.fiquepositivo.domain.exceptions;

public class ConflitoDeDadosException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public ConflitoDeDadosException(String mensagem) {
		super(mensagem);
	}

}
