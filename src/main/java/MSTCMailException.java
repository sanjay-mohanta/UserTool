//package mstc;

public class MSTCMailException extends Exception {

	private String message;

	public MSTCMailException(String message) {
		this.message = message;
	}
	
	public String getMessage() {
		return message;
	}
}
