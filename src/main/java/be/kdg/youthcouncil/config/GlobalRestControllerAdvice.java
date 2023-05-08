package be.kdg.youthcouncil.config;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


// Comes from here:
// https://salithachathuranga94.medium.com/validation-and-exception-handling-in-spring-boot-51597b580ffd
@RestControllerAdvice
public class GlobalRestControllerAdvice {

	@ExceptionHandler (MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String, List<String>>> handleValidationErrors(MethodArgumentNotValidException ex) {
		List<String> errors = ex.getBindingResult()
		                        .getFieldErrors()
		                        .stream()
		                        .map(FieldError::getDefaultMessage)
		                        .collect(Collectors.toList());
		return new ResponseEntity<>(getErrorsMap(errors), new HttpHeaders(), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler (ConstraintViolationException.class)
	public ResponseEntity<Map<String, String>> handleConstraintValidationErrors(ConstraintViolationException ex) {
		Map<String, String> errors = new HashMap<>();
		ex.getConstraintViolations().forEach(error -> errors.put(
				error.getPropertyPath().toString(),
				error.getMessage()
		));
		return new ResponseEntity<>(errors, new HttpHeaders(), HttpStatus.BAD_REQUEST);
	}


	private Map<String, List<String>> getErrorsMap(List<String> errors) {
		Map<String, List<String>> errorResponse = new HashMap<>();
		errorResponse.put("errors", errors);
		return errorResponse;
	}
}