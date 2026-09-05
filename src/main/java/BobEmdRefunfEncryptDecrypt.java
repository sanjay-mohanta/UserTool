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

public class BobEmdRefunfEncryptDecrypt {
	
	    
	    @SuppressWarnings("unused")
		private static String ENCODING_CHARSET = "UTF-8";

		public static final String ENCRYPT="ENCRYPT";
		public static final String DCRYPT="DCRYPT";
		public static final String RANDOM="RANDOM";

	public static void main(String[] args) throws Exception {
		/*if(args==null || args.length<4){
			System.out.println("Invalid input , please provide the correct input in right sequence");
		}
		String encMode=args[0];
		encMode=encMode==null ? "" : encMode;
		String key=args[1];
		key=key==null ? "" : key;
		String fromFile=args[2];
		fromFile=fromFile==null ? "" : fromFile;
		String toFile=args[3];
		toFile=toFile==null ? "" : toFile;
		
		if(!encMode.equals("D") && !encMode.equals("E")){
			System.out.println("Encryption Decryption mode must be eithe E or D. E for encryption and D for decryption");
			return;
		}
		
		if(key.length()<32){
			System.out.println("Key Length must be 32 character long");
			return;
		}
		
		if(fromFile.isEmpty()){
			System.out.println("Source file name should not be empty");
			return;
		}
		
		if(toFile.isEmpty()){
			System.out.println("Destination file name should not be empty");
			return;
		}
		
		if(encMode.equals("E")){
			encryptFile(fromFile, toFile, key);
			System.out.println("File encrypted successfully !!!!!");
		}else{
			decryptFile(fromFile, toFile, key);
			System.out.println("File decrypted successfully !!!!!");
		}*/
		//For Encryption
		//encryptFile();
		//System.out.println("File encrypted successfully !!!!!");
		
		//For Decryption
		decryptFile();
		System.out.println("File decrypted successfully !!!!!");
		
		
	}
	
	
	public static byte[] passwordBasedEncAES(byte[] pText, String password) throws Exception{
		byte[] result=null;
		int TAG_LENGTH_BIT = 128; 
		int IV_LENGTH_BYTE = 16;
		int SALT_LENGTH_BYTE = 16;
		int iterationCount=65536;
		int keyLength=256;
		String ENCRYPT_ALGO="AES/GCM/NoPadding";
		byte[] salt=null;

		byte[] iv =null;
		iv= getRandomNonce(IV_LENGTH_BYTE);
		salt = getRandomNonce(SALT_LENGTH_BYTE);

		// secret key from password
		SecretKey aesKeyFromPassword;
		try {
			aesKeyFromPassword = getAESKeyFromPassword(password, salt,iterationCount,keyLength);
			Cipher cipher = Cipher.getInstance(ENCRYPT_ALGO);
			// ASE-GCM needs GCMParameterSpec
			cipher.init(Cipher.ENCRYPT_MODE, aesKeyFromPassword, new GCMParameterSpec(TAG_LENGTH_BIT, iv));

			byte[] cipherText = cipher.doFinal(pText);

			// prefix IV and Salt to cipher text
			byte[] cipherTextWithIvSalt = ByteBuffer.allocate(iv.length + salt.length + cipherText.length)
					.put(iv)
					.put(cipherText)
					.put(salt)
					.array();
			result=cipherTextWithIvSalt;
		} catch (InvalidKeySpecException e) {
			throw new Exception("Incorrect or wrong key for encryption");

		}catch (BadPaddingException badPad) {
			throw new Exception("Incorrect or wrong key for encryption");
		}catch (Exception e) {
			throw new Exception("Error occurred while encryption");
		}
		return result;
	}
	
	public static byte[] passwordBasedDecryAES(byte[] cText, String password) throws Exception{
		int TAG_LENGTH_BIT = 128;
		int IV_LENGTH_BYTE = 16;
		int SALT_LENGTH_BYTE = 16;
		int iterationCount=65536;
		int keyLength=256;
		String ENCRYPT_ALGO="AES/GCM/NoPadding";
		byte[] salt=null;

		byte[] iv =null;
		byte[] result=null;
		// secret key from password
		SecretKey aesKeyFromPassword;
		try {
			ByteBuffer bb = ByteBuffer.wrap(cText);

			iv = new byte[IV_LENGTH_BYTE];
			bb.get(iv);
			byte[] cipherText = new byte[bb.remaining()-SALT_LENGTH_BYTE];
			bb.get(cipherText);
			salt = new byte[SALT_LENGTH_BYTE];
			bb.get(salt);
			aesKeyFromPassword = getAESKeyFromPassword(password, salt,iterationCount,keyLength);
			Cipher cipher = Cipher.getInstance(ENCRYPT_ALGO);
			// ASE-GCM needs GCMParameterSpec
			cipher.init(Cipher.DECRYPT_MODE, aesKeyFromPassword, new GCMParameterSpec(TAG_LENGTH_BIT, iv));

			result = cipher.doFinal(cipherText);

		} catch (InvalidKeySpecException e) {
			throw new Exception("Incorrect or wrong key for encryption");

		}catch (BadPaddingException badPad) {
			throw new Exception("Incorrect or wrong key for encryption");
		}catch (Exception e) {
			throw new Exception("Error occurred while decryption");
		}
		return result;
	}
	
	public static SecretKey getAESKeyFromPassword(String password, byte[] salt,
			int iterationCount,int keyLengthInByte)
            throws NoSuchAlgorithmException, InvalidKeySpecException {

        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");//"PBKDF2WithHmacSHA256"
        
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, iterationCount, keyLengthInByte);
        SecretKey secret = new SecretKeySpec(factory.generateSecret(spec).getEncoded(), "AES");
        return secret;

    }
	
	
	public static void encryptFile() throws Exception {

        // read a normal txt file
        byte[] fileContent = Files.readAllBytes(Paths.get("C:\\Users\\SANJAY MOHANTA\\OneDrive\\Desktop\\PTS-16958\\19-09-25\\058131955_Payment_Transaction_Report_190092025_17-12-021.csv"));
		String password="KEYFORCPPSFEDNETKEYFORCPPSFEDNET";
		String plainText="P,,MSTC100003,27940100020739,NEFT,000543,rmc buyer 2,1234567890,xxxx0234567,20000.00,04-02-2025,,,,,,NEFT,INR,,,,,,,,,,0\r\n" + 
				"P,,MSTC100003,27940100020739,NEFT,000531,MSTC SRO TEST,121212121,IOBA0000305,100000.00,06-07-2023,,,,,,NEFT,INR,,,,,,,,,,0\r\n" + 
				"P,,MSTC100003,27940100020739,NEFT,000532,Test Sys Buyer 1,123456,SBIN0003681,100.00,06-07-2023,,,,,,NEFT,INR,,,,,,,,,,0\r\n" + 
				"P,,MSTC100003,27940100020739,NEFT,000533,Koushik Mondal,7889457889,SBIN010212Q,100.00,07-09-2023,,,,,,NEFT,INR,,,,,,,,,,0\r\n" + 
				"P,,MSTC100003,27940100020739,NEFT,000535,Koushik Mondal,5623124578,SBIN0123240,1500.00,20-02-2024,,,,,,NEFT,INR,,,,,,,,,,0\r\n" + 
				"P,,MSTC100003,27940100020739,NEFT,000538,MSTC Ltd,5623457889,SBIN0212545,1200.00,20-02-2024,,,,,,NEFT,INR,,,,,,,,,,0\r\n" + 
				"P,,MSTC100003,27940100020739,NEFT,000545,SAI DINESH KUMAR TEKUMUDI,00000000004,SBIN0000451,100000.00,21-04-2025,,,,,,NEFT,INR,,,,,,,,,,0\r\n" + 
				"P,,MSTC100003,27940100020739,NEFT,000546,KINGSWOOD SUPPLIERS PRIVATE LIMITED,4568486256,SBIN0009236,500.00,21-05-2025,,,,,,NEFT,INR,,,,,,,,,,0";
		//byte[] fileContent =plainText.getBytes();
        // encrypt with a password
        byte[] encData = passwordBasedEncAES(fileContent, password);
        
        System.out.println("Encrypted data "+new String(encData));
        // save a file
        Path path = Paths.get("C:\\Users\\SANJAY MOHANTA\\OneDrive\\Desktop\\PTS-16958\\19-09-25\\encrypt\\058131955_Payment_Transaction_Report_190092025_17-12-021.csv");

        Files.write(path, encData);

    }

    public static void decryptFile() throws Exception {

        // read a file
    	String password="KEYFORCPPSFEDNETKEYFORCPPSFEDNET";
    	
        byte[] fileContent2 = Files.readAllBytes(Paths.get("D://051414700_Payment_Transaction_Report_01092026_22-00-15.csv"));
        System.out.println("fileContent2  "+new String(fileContent2));
        byte[] decryptedContent=passwordBasedDecryAES(fileContent2, password);
        System.out.println("Decrpted Data  "+new String(decryptedContent));
     // save a file
        //Path path = Paths.get(toFile);

        //Files.write(path, decryptedContent);

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

