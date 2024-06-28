package com.musinsa.product_app.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(DataNotFoundException.class)
	public ResponseEntity<ErrorResponse> handleNotFoundException(DataNotFoundException ex, WebRequest request) {
		ErrorResponse errorResponse = new ErrorResponse(
			HttpStatus.NOT_FOUND.value(),
			ex.getMessage(),
			null
		);
		return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler({MethodArgumentNotValidException.class})
	public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex, WebRequest request) {
		List<String> errors = new ArrayList<>();

		ex.getBindingResult().getFieldErrors().forEach(error -> errors.add(error.getDefaultMessage()));

		ErrorResponse errorResponse = new ErrorResponse(
			HttpStatus.BAD_REQUEST.value(),
			ex.getMessage(),
			errors
		);
		return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<ErrorResponse> handleGIllegalArgumentException(IllegalArgumentException ex, WebRequest request) {
		ErrorResponse errorResponse = new ErrorResponse(
				HttpStatus.BAD_REQUEST.value(),
				ex.getMessage(),
				null
		);
		return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handleGlobalException(Exception ex, WebRequest request) {
		ErrorResponse errorResponse = new ErrorResponse(
			HttpStatus.INTERNAL_SERVER_ERROR.value(),
			ex.getMessage(),
			null
		);
		return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
