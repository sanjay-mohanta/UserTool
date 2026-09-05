
public class Test {
	public static void main(String[] args) {
		String message = "The current ";
		message = message.substring(0, 100);
		//String message = "The current transaction has been rolled back because of a deadlock or timeout.  Reason code \"68\".. SQLCODE=-911, SQLSTATE=40001, DRIVER=4.25.13";

		if (message != null && message.length() > 100) {
			//message = message.substring(0, 100);
		}

		System.out.println(message);
	}
}
