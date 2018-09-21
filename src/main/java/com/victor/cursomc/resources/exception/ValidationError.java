package com.victor.cursomc.resources.exception;

import java.util.ArrayList;
import java.util.List;

public class ValidationError extends StandardError{
	private static final long serialVersionUID = 1L;
	
	private List<FieldMessage> errors = new ArrayList<FieldMessage>();
	
	public ValidationError(Integer statusHttp, String msg, Long timeStamp) {
		super(statusHttp, msg, timeStamp);
		
	}

	public List<FieldMessage> getErrors() {
		return errors;
	}


	public void addError(String fieldName, String message) 
	{
		errors.add(new FieldMessage(fieldName,message));
	}
	
	



}
