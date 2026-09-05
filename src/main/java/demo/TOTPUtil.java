package demo;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.ByteBuffer;
import java.security.SecureRandom;
import java.util.Base64;

public class TOTPUtil {

	private static final int TIME_STEP = 30; // 30 seconds
	private static final int DIGITS = 6;

	// Generate a random secret key (Base64)
	public static String generateSecret() {
		byte[] buffer = new byte[20];
		new SecureRandom().nextBytes(buffer);
		return Base64.getEncoder().encodeToString(buffer);
	}

	// Generate TOTP code
	public static String generateTOTP(String base64Secret) throws Exception {
		long timeIndex = System.currentTimeMillis() / 1000 / TIME_STEP;
		System.out.println("currentTimeMillis " + System.currentTimeMillis());
		return generateTOTP(base64Secret, timeIndex);
	}

	private static String generateTOTP(String base64Secret, long timeIndex) throws Exception {
		byte[] key = Base64.getDecoder().decode(base64Secret);
		//System.out.println("timeIndex " + timeIndex);
		ByteBuffer buffer = ByteBuffer.allocate(8);
		buffer.putLong(timeIndex);
		byte[] timeBytes = buffer.array();

		Mac mac = Mac.getInstance("HmacSHA1");
		mac.init(new SecretKeySpec(key, "HmacSHA1"));

		byte[] hash = mac.doFinal(timeBytes);

		int offset = hash[hash.length - 1] & 0x0F;
		int binary = ((hash[offset] & 0x7F) << 24) | ((hash[offset + 1] & 0xFF) << 16)
				| ((hash[offset + 2] & 0xFF) << 8) | (hash[offset + 3] & 0xFF);
		//System.out.println("binary " + binary);
		int otp = binary % (int) Math.pow(10, DIGITS);

		return String.format("%06d", otp);
	}

	// Verify TOTP (allows slight time drift)
	public static boolean verifyTOTP(String secret, String code) throws Exception {
		long currentTimeIndex = System.currentTimeMillis() / 1000 / TIME_STEP;
		//System.out.println("currentTimeIndex " + currentTimeIndex);
		for (int i = -1; i <= 1; i++) { // allow ±30 sec window
			String candidate = generateTOTP(secret, currentTimeIndex + i);
			System.out.println("candidate " + candidate);
			if (candidate.equals(code)) {
				return true;
			}
		}
		return false;
	}
}