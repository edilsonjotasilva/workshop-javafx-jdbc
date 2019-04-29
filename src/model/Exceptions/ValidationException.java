package model.Exceptions;

import java.util.HashMap;
import java.util.Map;

public class ValidationException extends RuntimeException{

	private static final long serialVersionUID = 1L;
	private Map<String, String>erros = new HashMap<>();
	
	public ValidationException (String str) {
		super(str);
	}
	public Map<String, String>pegaErros(){
		return erros;
	}
	public void addErrors(String fieldName, String errorMessage) {
		erros.put(fieldName, errorMessage);
	}
}
