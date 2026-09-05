package com.intellect.interfaces;

import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class AES256GCMEncDesc {

	private static String ENCODING_CHARSET = "UTF-8";

	public static final String ENCRYPT = "ENCRYPT";
	public static final String DCRYPT = "DCRYPT";
	public static final String RANDOM = "RANDOM";

	public static void main(String[] args) throws Exception {
		if (args == null || args.length < 4) {
			System.out.println("Invalid input , please provide the correct input in right sequence");
		}
		String encMode = args[0];
		encMode = encMode == null ? "" : encMode;
		String key = args[1];
		key = key == null ? "" : key;
		String fromFile = args[2];
		fromFile = fromFile == null ? "" : fromFile;
		String toFile = args[3];
		toFile = toFile == null ? "" : toFile;

		if (!encMode.equals("D") && !encMode.equals("E")) {
			System.out
					.println("Encryption Decryption mode must be eithe E or D. E for encryption and D for decryption");
			return;
		}

		if (key.length() < 32) {
			System.out.println("Key Length must be 32 character long");
			return;
		}

		if (fromFile.isEmpty()) {
			System.out.println("Source file name should not be empty");
			return;
		}

		if (toFile.isEmpty()) {
			System.out.println("Destination file name should not be empty");
			return;
		}

		if (encMode.equals("E")) {
			encryptFile(fromFile, toFile, key);
			System.out.println("File encrypted successfully !!!!!");
		} else {
			decryptFile(fromFile, toFile, key);
			System.out.println("File decrypted successfully !!!!!");
		}

	}

	public static byte[] passwordBasedEncAES(byte[] pText, String password) throws Exception {
		byte[] result = null;
		int TAG_LENGTH_BIT = 128;
		int IV_LENGTH_BYTE = 16;
		int SALT_LENGTH_BYTE = 16;
		int iterationCount = 65536;
		int keyLength = 256;
		String ENCRYPT_ALGO = "AES/GCM/NoPadding";
		byte[] salt = null;

		byte[] iv = null;
		iv = getRandomNonce(IV_LENGTH_BYTE);
		salt = getRandomNonce(SALT_LENGTH_BYTE);

		// secret key from password
		SecretKey aesKeyFromPassword;
		try {
			aesKeyFromPassword = getAESKeyFromPassword(password, salt, iterationCount, keyLength);
			Cipher cipher = Cipher.getInstance(ENCRYPT_ALGO);
			// ASE-GCM needs GCMParameterSpec
			cipher.init(Cipher.ENCRYPT_MODE, aesKeyFromPassword, new GCMParameterSpec(TAG_LENGTH_BIT, iv));

			byte[] cipherText = cipher.doFinal(pText);

			// prefix IV and Salt to cipher text
			byte[] cipherTextWithIvSalt = ByteBuffer.allocate(iv.length + salt.length + cipherText.length).put(iv)
					.put(cipherText).put(salt).array();
			result = cipherTextWithIvSalt;
		} catch (InvalidKeySpecException e) {
			throw new Exception("Incorrect or wrong key for encryption");

		} catch (BadPaddingException badPad) {
			throw new Exception("Incorrect or wrong key for encryption");
		} catch (Exception e) {
			throw new Exception("Error occurred while encryption");
		}
		return result;
	}

	public static byte[] passwordBasedDecryAES(byte[] cText, String password) throws Exception {
		int TAG_LENGTH_BIT = 128;
		int IV_LENGTH_BYTE = 16;
		int SALT_LENGTH_BYTE = 16;
		int iterationCount = 65536;
		int keyLength = 256;
		String ENCRYPT_ALGO = "AES/GCM/NoPadding";
		byte[] salt = null;

		byte[] iv = null;
		byte[] result = null;
		// secret key from password
		SecretKey aesKeyFromPassword;
		try {
			ByteBuffer bb = ByteBuffer.wrap(cText);

			iv = new byte[IV_LENGTH_BYTE];
			bb.get(iv);
			byte[] cipherText = new byte[bb.remaining() - SALT_LENGTH_BYTE];
			bb.get(cipherText);
			salt = new byte[SALT_LENGTH_BYTE];
			bb.get(salt);
			aesKeyFromPassword = getAESKeyFromPassword(password, salt, iterationCount, keyLength);
			Cipher cipher = Cipher.getInstance(ENCRYPT_ALGO);
			// ASE-GCM needs GCMParameterSpec
			cipher.init(Cipher.DECRYPT_MODE, aesKeyFromPassword, new GCMParameterSpec(TAG_LENGTH_BIT, iv));

			result = cipher.doFinal(cipherText);

		} catch (InvalidKeySpecException e) {
			throw new Exception("Incorrect or wrong key for encryption");

		} catch (BadPaddingException badPad) {
			throw new Exception("Incorrect or wrong key for encryption");
		} catch (Exception e) {
			throw new Exception("Error occurred while decryption");
		}
		return result;
	}

	public static SecretKey getAESKeyFromPassword(String password, byte[] salt, int iterationCount, int keyLengthInByte)
			throws NoSuchAlgorithmException, InvalidKeySpecException {

		SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");// "PBKDF2WithHmacSHA256"

		KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, iterationCount, keyLengthInByte);
		SecretKey secret = new SecretKeySpec(factory.generateSecret(spec).getEncoded(), "AES");
		return secret;

	}

	public static void encryptFile(String fromFile, String toFile, String password) throws Exception {

		// read a normal txt file
		byte[] fileContent = Files.readAllBytes(Paths.get(fromFile));

		// encrypt with a password
		byte[] encData = passwordBasedEncAES(fileContent, password);

		// save a file
		Path path = Paths.get(toFile);

		Files.write(path, encData);

	}

	public static void decryptFile(String fromEncryptedFile, String toFile, String password) throws Exception {

		// read a file
		byte[] fileContent = Files.readAllBytes(Paths.get(fromEncryptedFile));
		byte[] decryptedContent = passwordBasedDecryAES(fileContent, password);
		// save a file
		Path path = Paths.get(toFile);

		Files.write(path, decryptedContent);

	}

	public static byte[] getRandomNonce(int numBytes) {
		byte[] nonce = new byte[numBytes];
		new SecureRandom().nextBytes(nonce);
		return nonce;
	}

	public static SecretKey getAESKeyFromPassword(char[] password, byte[] salt)
			throws NoSuchAlgorithmException, InvalidKeySpecException {

		SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");

		KeySpec spec = new PBEKeySpec(password, salt, 65536, 256);
		SecretKey secret = new SecretKeySpec(factory.generateSecret(spec).getEncoded(), "AES");
		return secret;

	}

}
