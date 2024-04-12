package com.thbs.BatchManagement.exceptionhandler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(DuplicateBatchFoundException.class)
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<String> duplicateBatchFoundException(DuplicateBatchFoundException ex) {
		return new ResponseEntity<String>(ex.getMessage(), HttpStatus.OK);
	}

	@ExceptionHandler(BatchNotFoundException.class)
	public ResponseEntity<String> batchNotFoundException(BatchNotFoundException ex) {
		return new ResponseEntity<String>(ex.getMessage(), HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(BatchEmptyException.class)
	public ResponseEntity<String> batchEmptyException(BatchEmptyException ex) {
		return new ResponseEntity<String>(ex.getMessage(), HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(EmployeeNotFoundException.class)
	public ResponseEntity<String> employeeNotFoundException(EmployeeNotFoundException ex) {
		return new ResponseEntity<String>(ex.getMessage(), HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(InvalidDateFormatException.class)
	public ResponseEntity<String> invalidDateFormatException(InvalidDateFormatException ex) {
		return new ResponseEntity<String>(ex.getMessage(), HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(IOException.class)
	public ResponseEntity<String> iOException(IOException ex) {
		return new ResponseEntity<String>(ex.getMessage(), HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(ParseException.class)
	public ResponseEntity<String> parseException(ParseException ex) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error parsing date");
	}

	@ExceptionHandler(JsonProcessingException.class)
	public ResponseEntity<String> jsonProcessingException(JsonProcessingException ex) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error processing JSON data");
	}
	
	@ExceptionHandler(EmptyEmployeesListException.class)
	public ResponseEntity<String> emptyEmployeesListException(EmptyEmployeesListException ex) {
		return new ResponseEntity<String>(ex.getMessage(), HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(DuplicateEmployeeException.class)
	public ResponseEntity<String> duplicateEmployeeException(DuplicateEmployeeException ex) {
		return new ResponseEntity<String>(ex.getMessage(), HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(EmptyFileException.class)
	public ResponseEntity<String> emptyFileException(EmptyFileException ex) {
		return new ResponseEntity<String>(ex.getMessage(), HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(DateTimeException.class)
    public ResponseEntity<Object> handleDateTimeException(DateTimeException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                             .body("Invalid date: " + ex.getMessage());
    }

}