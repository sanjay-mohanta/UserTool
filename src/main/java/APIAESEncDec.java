
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class APIAESEncDec {

	private static final String IV="cM5ApI1nitVect0r";
	public static void main(String[] args) throws Exception {
		APIAESEncDec obj = new APIAESEncDec();
		String result="";
		/*String plainText="P,,MSTC100003,27940100020739,NEFT,000543,rmc buyer 2,1234567890,xxxx0234567,20000.00,04-02-2025,,,,,,NEFT,INR,,,,,,,,,,0\r\n" + 
				"P,,MSTC100003,27940100020739,NEFT,000531,MSTC SRO TEST,121212121,IOBA0000305,100000.00,06-07-2023,,,,,,NEFT,INR,,,,,,,,,,0\r\n" + 
				"P,,MSTC100003,27940100020739,NEFT,000532,Test Sys Buyer 1,123456,SBIN0003681,100.00,06-07-2023,,,,,,NEFT,INR,,,,,,,,,,0\r\n" + 
				"P,,MSTC100003,27940100020739,NEFT,000533,Koushik Mondal,7889457889,SBIN010212Q,100.00,07-09-2023,,,,,,NEFT,INR,,,,,,,,,,0\r\n" + 
				"P,,MSTC100003,27940100020739,NEFT,000535,Koushik Mondal,5623124578,SBIN0123240,1500.00,20-02-2024,,,,,,NEFT,INR,,,,,,,,,,0\r\n" + 
				"P,,MSTC100003,27940100020739,NEFT,000538,MSTC Ltd,5623457889,SBIN0212545,1200.00,20-02-2024,,,,,,NEFT,INR,,,,,,,,,,0\r\n" + 
				"P,,MSTC100003,27940100020739,NEFT,000545,SAI DINESH KUMAR TEKUMUDI,00000000004,SBIN0000451,100000.00,21-04-2025,,,,,,NEFT,INR,,,,,,,,,,0\r\n" + 
				"P,,MSTC100003,27940100020739,NEFT,000546,KINGSWOOD SUPPLIERS PRIVATE LIMITED,4568486256,SBIN0009236,500.00,21-05-2025,,,,,,NEFT,INR,,,,,,,,,,0";
		result=obj.encryptAES256(plainText, "EKYLdkkW1ZCF7hKY34TTBA==");
		System.out.println("Encrypted Data :"+result);*/
		//String data="/bmyyn4VV4H7/sh4rHZgKlNfleFovSDkItEB30ERi6c=";
		result="OZOh3cX9DKynZsrub2+nhoggjceNg1EqhfmjDdHMMIc40L4rlmYJ5FTlkGuEBrfkXVr+wkSaH7VEcWNce08wuOC2yNSJjxxqsoXb4XO1bW/mDNJaL7nDKY1+NTAZbyWbffzdqMHMuMQQjWG4hjMOaWZ2TvquzyNE3vj1yiF1qrT4HVzkhTskbaemOGJja+9NnjfMh5uPuUGDZwObCwoy8I1NCs2BkCkwn496/r8ysXnWptX3ucst2Wz9PRlMUszmxxX223jQg5EdwxhA1osVDYbqBKUkGSJ/4OqQ6RI98auYjkOecGdQF/tOiiQp77HSVr5zycI8NjzI/ZG0Iqjx1FcFjK5SL6KysWASsw9x8sffSGbIMYBLp5MyP43ow5zzbWR7qb+JTgq5Kheochgex12udxM60NnzOWgEop/j6eAqPbdhVkQbn3INrQeU4VjrSN7YxRFJtLhDW6zxKdCsaozPkvw8LA7KultF++NbBv4VFx/Rm71f8R9XWZwGuzF1l1sgQnpAunqh5I6uo/fsDB7u2J+tVIV4MvNu0Ppt0Rdv56LSr4ZRc2L7+ztOnLpnUlgNzAlA1AneRrCS2L7RKxWBq4qHHVy1kutaikPSWVGqU7XPLNjJjtsbDVtwSnQ2O3roUSDUIygI+0AphNwjbnDK5FIr6NZmrC2mbcFExAYQbcP7QS3gMI9GvvpSLZ1zKgkMzqRHriwyZlG5+TZBzwv55VfPHfGHaC2dpe9JBdmEWNERDvvCm0KLeouvz7TKYf3vNYYILZWNO8HDCxouk5mLl31FC2x7xmANiG+jMhob52M4L7HZJkTsYw2ZHd0/b0uuoO3f2daTyk+2NT8JHWT1jbBcs5gULRYl3gB46uD3uS6ECTdzGDTIVAgyAVrVHYncg7Lw2L0B+Og61jb5FmvarymzjoUWw8La5pO/WdOVna9lPDhKy1IS7x3oERy24YxdC+gD9YMC8RsQNioZOXhYWI5xFbllNVOn7GHvf66JWEW1RDkuGR/UgKS2ZOp2tZga3L51QwwT42dzF9l/J7f4IdhQEzX/osxQ7KKoGEGJdc/SgPBvL0qEG5GMrn67ezYpENo7JSEt97cYz1CtVVxllIOqnylFXIaMPdtU5mFpCLmwIn+CxiM7mInAg16x2zTRrLFzuRTpDW+gzr+dQAuzmED7dm3WUGLi4ZNY3sZfWpeaJ47bl+qdM5EuDhIaKOBsKDKUqjip/ceIqD58JBsk2Da1wf06RQkmYWl4qvIzNX5aSJzz5H8iX+PgAPdIHqWpJSjUXAFWZKMhn7u0zHj8FR3zqb8776p3HEKwoNM=";
		String output=obj.decryptAES256(result, "KEYFORCPPSFEDNETKEYFORCPPSFEDNET");
		System.out.println("Decrypted Data :"+output);
	}
	
	public String decryptAES256(String data, String key) throws Exception {
		KeyGenerator keyGenerator = null;
		try {
			keyGenerator = KeyGenerator.getInstance("AES");
		} catch (NoSuchAlgorithmException e) {
			System.out.println("Exception occurred while initializing the key !!!!");
			throw e;
		}
		keyGenerator.init(256);

		byte[] encodedKey = key.getBytes();
		SecretKey secKey = new SecretKeySpec(encodedKey,"AES");

		byte[] ivBytes = IV.getBytes();
		String decryptedText = null;
		try {
			byte[] decoded=Base64.getDecoder().decode(data);
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			SecretKeySpec keySpec = new SecretKeySpec(secKey.getEncoded(), "AES");
			IvParameterSpec ivSpec = new IvParameterSpec(ivBytes);
			cipher.init(Cipher.DECRYPT_MODE, keySpec,ivSpec);
			byte[] cipherText = cipher.doFinal(decoded);
			decryptedText = new String(cipherText);
		} catch (Exception e) {
			System.out.println("Exception occurred while decryption !!!!");
			throw e;
		}
		return decryptedText;
	}

	public String encryptAES256(String data,String key) throws Exception {
		byte[] plaintext=data.getBytes();
		KeyGenerator keyGenerator = null;
		String result="";
		try {
			keyGenerator = KeyGenerator.getInstance("AES");
		} catch (NoSuchAlgorithmException e) {
			System.out.println("Exception occurred while initializing the key !!!!");
			throw e;
		}
		keyGenerator.init(256);

		byte[] encodedKey = key.getBytes();
		SecretKey secKey = new SecretKeySpec(encodedKey,"AES");

		byte[] ivBytes = IV.getBytes();
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		SecretKeySpec keySpec = new SecretKeySpec(secKey.getEncoded(), "AES");
		IvParameterSpec ivSpec = new IvParameterSpec(ivBytes);
		cipher.init(Cipher.ENCRYPT_MODE, keySpec,ivSpec);
		byte[] cipherText = cipher.doFinal(plaintext);
		result=Base64.getEncoder().encodeToString(cipherText);
		return result;
	}
}
