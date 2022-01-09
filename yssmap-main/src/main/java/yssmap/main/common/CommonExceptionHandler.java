package yssmap.main.common;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import yssmap.main.dto.ExceptionResponse;

@RestControllerAdvice
public class CommonExceptionHandler {

	@ExceptionHandler(IllegalArgumentException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ResponseEntity handleIllegalArgumentException(HttpServletRequest request, Exception ex) {
		System.out.println(ex.getMessage());
		return new ResponseEntity<>(new ExceptionResponse(ex.getMessage()),
			getHeader(), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(IllegalStateException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ResponseEntity handleIllegalStateException(HttpServletRequest request, Exception ex) {
		System.out.println(ex.getMessage());
		return new ResponseEntity<>(new ExceptionResponse(ex.getMessage()),
			getHeader(), HttpStatus.BAD_REQUEST);
	}

	private HttpHeaders getHeader(){
		HttpHeaders responseHeader=new HttpHeaders();
		responseHeader.set("Content-Type","application/json; charset=utf8");
		return responseHeader;
	}
}
