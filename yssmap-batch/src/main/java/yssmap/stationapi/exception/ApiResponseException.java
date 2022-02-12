package yssmap.stationapi.exception;

public class ApiResponseException extends RuntimeException{

	public ApiResponseException(String message) {
		super(message);
	}

	public ApiResponseException(Throwable cause) {
		super(cause);
	}
}
