package demo;

public class TestTOTP {
	 public static void main(String[] args) throws Exception {

	        // Step 1: Generate secret (store this!)
	        String secret = TOTPUtil.generateSecret();
	        System.out.println("Secret: " + secret);

	        // Step 2: Generate current OTP
	        String otp = TOTPUtil.generateTOTP(secret);
	        System.out.println("Current OTP: " + otp);

	        // Step 3: Verify OTP
	        boolean valid = TOTPUtil.verifyTOTP(secret, "880864");
	        System.out.println("Is valid: " + valid);
	    }
}
