package es.ucm.fdi.tp.exceptions;

public class ParameterException extends Exception {
	
	private static final long serialVersionUID = 1L;
	
	private String message;
	
	public ParameterException(String cadena){
		this.message = cadena;
	}
	public String toString(){
		return this.message;
	}
}
