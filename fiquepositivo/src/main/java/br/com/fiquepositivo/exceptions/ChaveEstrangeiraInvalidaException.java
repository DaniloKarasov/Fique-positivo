package br.com.fiquepositivo.exceptions;

public class ChaveEstrangeiraInvalidaException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public ChaveEstrangeiraInvalidaException(String mensagem) {
		super(mensagem);
	}
}
