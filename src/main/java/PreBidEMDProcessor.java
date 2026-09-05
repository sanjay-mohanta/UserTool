
import java.net.SocketException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.util.*;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import com.enterprisedt.net.ftp.FTPException;

public class PreBidEMDProcessor {

 
   
	private static final String FTPURL = "10.1.5.30";
	private static final int FTPPORT = 21;
	private static final String USER = "bob";
	private static final String PASS = "Bob@Bank";
	private static final String FTP_BASE_DIR = "BOBRefundResponse";
	
    public static void main(String[] args) {
        try (Connection conn = connect()) {
        	
        	System.out.println("******Processing of Offline BOB of ACK or NACK files started at :  " + new java.util.Date() + "******");
        	//ACK file processing
            processAckFiles(conn);
            
            //NACK file processing
            processNackFiles(conn);
        } catch (Exception e) {
            logError("Main processing error", e);
        }
    }
    
	private static Connection connect() {
		Connection connection=null;
		try {
			Class.forName("com.ibm.db2.jcc.DB2Driver");
			//change before porting to live
			//connection = DriverManager.getConnection("jdbc:db2://localhost:50001/DBMSTC", "db2instn", "mstc@123");
			//connection = DriverManager.getConnection("jdbc:db2://10.1.6.85:50000/DBMSTC","db2inst1", "db2inst1");
			connection=DriverManager.getConnection("jdbc:db2://10.1.14.86:50000/DBMSTC", "db2inst1", "db2inst1");
			final Statement stmt = connection.createStatement();
			stmt.execute("set schema scrap");
			stmt.close();

		} catch (final Exception exception) {
			System.out.println("Connection Exception" + exception);
		}
		return connection;
	}

	
	private static boolean checkBatchUpdateCounts(int[] updateCounts) {
		boolean updateSuccess = true;
		for (int i = 0; i < updateCounts.length; i++) {
			if (updateCounts[i] == Statement.EXECUTE_FAILED) {
				updateSuccess = false;
				break;
			}
		}
		return updateSuccess;
	}
	
	private static boolean executeBatchAndValidate(PreparedStatement ps, int expectedCount, String batchName) throws SQLException {
	    int[] results = ps.executeBatch();
	    System.out.println(batchName + " results: " + Arrays.toString(results));

	    int successCount = 0;
	    for (int r : results) {
	        if (r >= 0 ) {//|| r == Statement.SUCCESS_NO_INFO need to check
	            successCount=successCount+r;
	        }
	    }
	    if (successCount != expectedCount) {
	        logError(batchName + " batch count mismatch: expected=" + expectedCount + ", success=" + successCount, null);
	        return false;
	    }
	    return checkBatchUpdateCounts(results);
	}


    // -------------------------------
    //  ACK and NACK Process Methods
    // -------------------------------
    private static void processAckFiles(Connection conn) throws Exception {
        String ackFile = getFirstFileName("_ACK");
        String nackFile = getFirstFileName("_NACK");
        if (ackFile == null) {
            logInfo("No ACK file found");
            return;
        }
        logInfo("ACK File "+ackFile);
        //if nack file found along with ack file then process nack file
        if (nackFile != null) {
            logInfo("NACK file found while processing ACK file");
            return;
        }
        //As per PTS 23377 changes are done if only ack file present and no nack file is present then only process ack file
        if (nackFile == null && ackFile != null) {

        try (InputStream is = readFtpFile(ackFile)) {
            List<String> headers = getAckFileTags();
            Map<Integer, Map<String, String>> fileData = parseCsvFirstRow(is, ackFile, headers);
            logInfo("fileData "+fileData);
            String requestFileName = extractFileName(fileData);
            logInfo("requestFileName "+requestFileName);
            String requestFilestatus = extractFileStatus(fileData);
            logInfo("requestFilestatus "+requestFilestatus);
            
            String reqfileinReqFolder = getRequestFilename();
			System.out.println("reqfileinReqFolder "+reqfileinReqFolder);
            
            if(requestFilestatus.equals("ACPT") && requestFileName!=null){// request file is accepted returned in ACK file 
            boolean updated = updateACKData(requestFileName, conn);
            logInfo("updated "+updated);
            if (updated) {
            	writeACKDatatoFtpNew(ackFile);
            	 logInfo("ACK file Moved ");
                deleteFtpFile(ackFile);
                logInfo("ACK file Deleted ");
                
                
                //once ack is generated without nack then we have to remove request file so that again bank do not able to process request file
                if (requestFileName.trim().equals(reqfileinReqFolder.trim())) {
                	
                	System.out.println("Request file delete start after ACK check");													
					writeReqDatatoFtpNew(requestFileName);
					deleteReqFtpFile(requestFileName);
					System.out.println("Request file deleted after ACK check");
                }
                
            }
            }
        }
        }else {
        	 logInfo("NACK file found while processing ACK file ");
             return;
        }
    }
    
	public static String getMailBody(String refund_id, Connection connection, String res)
			throws SQLException {
		String header = "<!DOCTYPE html><html lang=en><head><style>table.tabGreen {font-family: verdana, arial, sans-serif;font-size: 11px;"
				+ "color: #333333;border-width: 1px;border-color: #E9E9E9;border-collapse: collapse;margin-top: 25px;width: auto;margin-left:auto;margin-right:auto;}"
				+ "table.tabGreen tr {padding: 6px;background-color: #FFFFFF;border: 1px solid #ddd;text-align:center;}"
				+ "table.tabGreen td{padding: 6px;color: #003366;border: 1px solid #ddd;text-align:center;}"
				+ "table.tabGreen th {text-align:center;font-family:Tahoma, Geneva, sans-serif;"
				+ "font-weight:bold;font-size:12px;border: 1px solid #ddd;background-color:#4CAF50;color:#FFF;"
				+ "padding:2px 2px 5px 5px;}</style></head>"
				+ "<body><div align=center><table class=tabGreen width=100%>"
				+ "<th>Comp. Name</th><th>Refund Id</th><th>Amount</th><th>Reason</th>";
		String body = "";
		
			
		String response = res;
		String amount = "";
		
		System.out.println("data response" + response);
		
		String sql="SELECT AMT FROM EMD_REFUND_REQUEST WHERE REF_ID=?";
		PreparedStatement pst= connection.prepareStatement(sql);
		pst.setString(1, refund_id);
		ResultSet rst = pst.executeQuery();
		if (rst.next()) {
			amount=rst.getString(1);
		}
		rst.close();
		pst.close();
		System.out.println("data amount" + amount);
		String mailData = getMailData(refund_id, connection, amount, response);
		System.out.println("data mailData" + mailData);
		body = body + mailData;
	

		String footer = "</table></div>";
		String retval = header + body + footer;
		String finalData = "<br><font face='verdana' size='4'><U>Failed EMD Refund Transaction Deatils</U></font><br><br><font face='verdana' size='2'>Dear Sir,"
				+ " <br><br>Below mentioed emd refund transactions failed to reconcile by our system.Please check. <br><br>"
				+ retval + "<br>" + "<br>You are requested to take appropriate action at the earliest.<br>"
				+ "<br><p><font face='Verdana' size='4'>From:</font><br>MSTC eAuction Team</font></p><br>"
				+ "<font face='Verdana' size='4'>This is a system generated mail.Please do not reply.</font></body></html>";

		System.out.println("finalData" + finalData);
		return finalData;
	}
	


    
    public static String getMailData(String rtgsId, Connection connection, String amt, String res) throws SQLException {
		String sqldata = "Select Comp_Name from EMD_REFUND_REQUEST a,BUYER_MASTER b"
				+ " where a.REF_ID=?  and a.BUYER_REF_ID=b.BUYER_REF_ID";
		String output = "";
		System.out.println("rtgsId" + rtgsId);
		if (rtgsId != null && !rtgsId.equals("")) {
			PreparedStatement pstmt = connection.prepareStatement(sqldata);
			pstmt.setString(1, rtgsId);
			ResultSet rst = pstmt.executeQuery();
			if (rst.next()) {
				output = "<tr><td>" + rst.getString(1) + "</td><td>" + rtgsId + "</td><td>" + amt + "</td><td>" + res
						+ "</td></tr>";
			}
			rst.close();
			pstmt.close();

			System.out.println("output" + output);
		} else {
			output = "Blank Remitter Code";
		}
		if (output.equals(""))
			output = "<tr><td>No Data</td><td>" + rtgsId + "</td><td>" + amt + "</td><td>" + res + "</td></tr>";
		else if (output.equals("Blank Remitter Code"))
			output = "<tr><td>No Data</td><td>Blank Remitter Code</td><td>" + amt + "</td><td>" + res + "</td></tr>";
		return output;
	}
    
    

    private static void processNackFiles(Connection conn) throws Exception {
        String nackFile = getFirstFileName("_NACK");
        String ackFile = getFirstFileName("_ACK");
        //As per PTS 23377 an ACK file will always be sent before sending nack file
        if (nackFile == null && ackFile!=null) {
            logInfo("No NACK file found");
            return;
        }
        if (nackFile == null && ackFile == null) {
            logInfo("No NACK and ACK file found");
            return;
        }
        
        if(nackFile != null && ackFile != null) {
        	System.out.println("NACK file "+nackFile);
        	System.out.println("ACK file "+ackFile);

        try (InputStream is = readFtpFile(nackFile)) {
            List<String> fileTags = getNackFileLevelTags();
            Map<Integer, Map<String, String>> fileData = parseCsvFirstRow(is, nackFile, fileTags);
            String requestFileName = extractFileName(fileData);
            System.out.println("requestFileName "+requestFileName);
            List<String> txnTags = getNackTransactionLevelTags();
            Map<Integer, Map<String, String>> txnData = parseCsvTransactions(readFtpFile(nackFile), nackFile, txnTags);
            Map<String, String> customerRefs = getCustRefData(txnData);
            System.out.println("customerRefs "+customerRefs);
            
            String reqfileinReqFolder = getRequestFilename();
         	System.out.println("reqfileinReqFolder "+reqfileinReqFolder);
            
            int updatedReq = updateNACKData(customerRefs, requestFileName, conn);
            int updatedWallet = updatedReq > 0 ? updateNACKDataWalletNew(requestFileName, conn) : 0;

            logInfo("updatedReq "+updatedReq);
            logInfo("updatedWallet "+updatedWallet);
            if (updatedReq > 0 && updatedWallet > 0) {
            	writeACKDatatoFtpNew(ackFile);
            	logInfo("ACK file Moved ");
                deleteFtpFile(ackFile);
                logInfo("ACK file Deleted ");
                
            	writeNACKDatatoFtpNew(nackFile);
            	logInfo("NACK file Moved ");
                deleteFtpFile(nackFile);
                logInfo("NACK file Deleted ");
                
                if (requestFileName.trim().equals(reqfileinReqFolder.trim())) {
                	
                	System.out.println("Request file delete start after NACK check");													
					writeReqDatatoFtpNew(requestFileName);
					deleteReqFtpFile(requestFileName);
					System.out.println("Request file deleted after NACK check");
                }
                
                
                System.out.println("Mail Started ");
                MSTCMail email = new MSTCMail();
    			email.setSmtpServer("10.1.5.30");
    			
    			Map<String, String> customerRefIds = new HashMap<String, String>();
    			
    			String sql="SELECT CUSTOMER_REF_NO, COALESCE(REJECT_REASON,'NACK Received from BOB') FROM BANK_SCRAP_EMD_REFUND_REQUEST_RESPONSE WHERE REQUEST_FILE_NAME=? ";
    			PreparedStatement pstmt = conn.prepareStatement(sql);
    			pstmt.setString(1, requestFileName);
    			ResultSet rst = pstmt.executeQuery();
    			while (rst.next()) {
    				customerRefIds.put(rst.getString(1), rst.getString(2));
    			}
    			rst.close();
    			pstmt.close();
    			
    			System.out.println("customerRefIds "+customerRefIds);
    			
    			//Sending mails to buyer getting NACK for its related request file
    			if(!customerRefIds.isEmpty()){
	    		    for (Map.Entry<String, String> entry : customerRefIds.entrySet()) {
	    		    	try {
		    		    	String refId=entry.getKey();
			                String res=entry.getValue();
			                
		    		    	System.out.println("refId: " + refId);
		                    System.out.println("res: " + res);
		                  
		                    System.out.println("Mail Send Started ");
		    				String argsCCwale[] = new String[5];
		    				argsCCwale[0] = "crgiri@mstcindia.co.in";
		    				argsCCwale[1] = "systems@mstcindia.co.in";
		    				argsCCwale[2] = "inderpalsingh@mstcindia.co.in";
		    				argsCCwale[3] = "nmohan@mstcindia.co.in";
		    				argsCCwale[4] = "aksingh@mstcindia.co.in";
		    				
		    				String mailbody = getMailBody(refId, conn, res);
		    				System.out.println("mailbody " + mailbody);
		    				email.sendMailWithMultiCC("admin@mstcecommerce.com", "crgiri@mstcecommerce.com", argsCCwale,
		    						"EMD Refund Failed", mailbody);
		    				System.out.println("------------Admin Mail send Successfully------------");
	    		    	} catch (Exception mailEx) {
	    		             logError("Mail sending failed for refId: " + entry.getKey(), mailEx);
	    		        }
	    				
	                  
	                }
    			}

                
            }
			
        }
        }else {
        	logInfo("No NACK and ACK file found");
            return;
        }
    }

    // -------------------------------
    // FTP Utilities
    // -------------------------------
    
	public static FTPClient createFtpConnection(String path) throws SocketException, IOException {
		FTPClient ftpClient = new FTPClient();
		ftpClient.connect(FTPURL, FTPPORT);
		ftpClient.login(USER, PASS);
		ftpClient.enterLocalPassiveMode();
		ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
		ftpClient.changeWorkingDirectory(path);
		return ftpClient;
	}
	
	public static FTPClient createFtpConnectionEditJNew(String path) throws SocketException, IOException {

	    FTPClient ftpClient = new FTPClient();
	    ftpClient.connect(FTPURL);

	    int replyCode = ftpClient.getReplyCode();
	    if (!FTPReply.isPositiveCompletion(replyCode)) {
	        ftpClient.disconnect();
	        throw new IOException("FTP server refused connection, replyCode: " + replyCode);
	    }

	    boolean loginSuccess = ftpClient.login(USER, PASS);
	    if (!loginSuccess) {
	        ftpClient.disconnect();
	        throw new IOException("FTP login failed");
	    }

	    ftpClient.enterLocalPassiveMode();
	    ftpClient.setFileType(FTP.BINARY_FILE_TYPE);

	    boolean changedDir = ftpClient.changeWorkingDirectory(path);
	    if (!changedDir) {
	        ftpClient.logout();
	        ftpClient.disconnect();
	        throw new IOException("Failed to change working directory to: " + path);
	    }

	    return ftpClient;
	}

	public static com.enterprisedt.net.ftp.FTPClient createFtpConnectionEditJ(String path)
			throws SocketException, IOException, FTPException {
		com.enterprisedt.net.ftp.FTPClient fcget = new com.enterprisedt.net.ftp.FTPClient();
		fcget.setRemoteHost(FTPURL);
		fcget.connect();
		fcget.login(USER, PASS);
		fcget.setDetectTransferMode(true);
		fcget.chdir(path);
		return fcget;
	}

	public static void destroyFtpConnection(FTPClient ftpClient) throws SocketException, IOException {
		ftpClient.logout();
		ftpClient.disconnect();
	}
    private static String getFirstFileName(String filter) throws IOException {
        FTPClient ftp = createFtpConnection(FTP_BASE_DIR); 
        String filename = null;
	    try {
	        FTPFile[] files = ftp.listFiles();

	        // Collect only files contains with "filter" parameter
	        List<String> filterfiles = new ArrayList<>();
	        for (FTPFile file : files) {
	            if (!file.isDirectory()  && file.getName().contains(filter) ) {
	            	filterfiles.add(file.getName());
	            }
	        }

	        if (filterfiles.isEmpty()) {
	        	logInfo("No file ending with "+filter+" found in FTP directory.");
	            
	        } else if (filterfiles.size() > 1) {
	        	logInfo("Multiple files ending with "+filter+" found in FTP directory. Expected only one.");
	            
	        } else {
	            filename = filterfiles.get(0);
	        }
	    } finally {
	        destroyFtpConnection(ftp);
	    }

	    return filename;
    }
    

	public static void writeReqDatatoFtpNew(String filename)
	        throws SocketException, IOException {

	    FTPClient ftpClient = createFtpConnectionEditJNew("BOBRefundRequest");

	    // Download existing file into byte array
	    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
	    boolean success = ftpClient.retrieveFile(filename, outputStream);
	    if (!success) {
	        ftpClient.logout();
	        ftpClient.disconnect();
	        throw new IOException("Failed to retrieve file: " + filename);
	    }
	    byte[] fileBytes = outputStream.toByteArray();
	    outputStream.close();

	    ftpClient.logout();
	    ftpClient.disconnect();

	    // Reconnect to upload to "updated" folder
	    ftpClient = createFtpConnectionEditJNew("BOBRefundRequest/updated");

	    // Upload original file
	    try (ByteArrayInputStream inputStream = new ByteArrayInputStream(fileBytes)) {
	        success = ftpClient.storeFile(filename, inputStream);
	        if (!success) {
	            throw new IOException("Failed to upload file: " + filename);
	        }
	    }

	   
	    ftpClient.logout();
	    ftpClient.disconnect();
	}
	

	public static void deleteReqFtpFile(String filename) throws SocketException, IOException, FTPException {
		com.enterprisedt.net.ftp.FTPClient fcget = createFtpConnectionEditJ("BOBRefundRequest");
		fcget.delete(filename);
		fcget.quit();
	}

    
    
    public static String getRequestFilename() throws SocketException, IOException {
		FTPClient ftpClient = createFtpConnection("BOBRefundRequest");
		FTPFile[] files = ftpClient.listFiles();
		String filename = "";
		for (FTPFile file : files) {
			if (!file.isDirectory()) {
				filename = file.getName();
			}

		}
		destroyFtpConnection(ftpClient);
		return filename;
	}
    
    public static SecretKey getAESKeyFromPassword(String password, byte[] salt,
			int iterationCount,int keyLengthInByte)
            throws NoSuchAlgorithmException, InvalidKeySpecException {

        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");//"PBKDF2WithHmacSHA256"
        
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, iterationCount, keyLengthInByte);
        SecretKey secret = new SecretKeySpec(factory.generateSecret(spec).getEncoded(), "AES");
        return secret;

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
    private static InputStream DecryptFtpFile(InputStream inputStream) throws Exception {
    	byte[] fileContent2=IOUtils.toByteArray(inputStream);
    	System.out.println("Encrypted Data  "+new String(fileContent2));
	    String password="KEYFORCPPSFEDNETKEYFORCPPSFEDNET";
	    byte[] decryptedContent=passwordBasedDecryAES(fileContent2, password);
	    System.out.println("Decrpted Data  "+new String(decryptedContent));
	    InputStream outStream = new ByteArrayInputStream(decryptedContent);
	    return outStream;
    }
    private static InputStream readFtpFile(String filename) throws Exception {
        FTPClient ftp = createFtpConnection(FTP_BASE_DIR);
        InputStream inputStream=ftp.retrieveFileStream(filename);
        return DecryptFtpFile(inputStream);
    }
    
	
    
	public static void writeACKDatatoFtpNew(String filename)
	        throws SocketException, IOException {

	    FTPClient ftpClient = createFtpConnectionEditJNew("BOBRefundResponse");

	    // Download existing file into byte array
	    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
	    boolean success = ftpClient.retrieveFile(filename, outputStream);
	    if (!success) {
	        ftpClient.logout();
	        ftpClient.disconnect();
	        throw new IOException("Failed to retrieve file: " + filename);
	    }
	    byte[] fileBytes = outputStream.toByteArray();
	    outputStream.close();

	    ftpClient.logout();
	    ftpClient.disconnect();

	    // Reconnect to upload to "updated" folder
	    ftpClient = createFtpConnectionEditJNew("BOBRefundResponse/acknowledgement");

	    // Upload original file
	    try (ByteArrayInputStream inputStream = new ByteArrayInputStream(fileBytes)) {
	        success = ftpClient.storeFile(filename, inputStream);
	        if (!success) {
	            throw new IOException("Failed to upload file: " + filename);
	        }
	    }

	   
	    ftpClient.logout();
	    ftpClient.disconnect();
	}
	
	public static void writeNACKDatatoFtpNew(String filename)
	        throws SocketException, IOException {

	    FTPClient ftpClient = createFtpConnectionEditJNew("BOBRefundResponse");

	    // Download existing file into byte array
	    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
	    boolean success = ftpClient.retrieveFile(filename, outputStream);
	    if (!success) {
	        ftpClient.logout();
	        ftpClient.disconnect();
	        throw new IOException("Failed to retrieve file: " + filename);
	    }
	    byte[] fileBytes = outputStream.toByteArray();
	    outputStream.close();

	    ftpClient.logout();
	    ftpClient.disconnect();

	    // Reconnect to upload to "updated" folder
	    ftpClient = createFtpConnectionEditJNew("BOBRefundResponse/nonacknowledgement");

	    // Upload original file
	    try (ByteArrayInputStream inputStream = new ByteArrayInputStream(fileBytes)) {
	        success = ftpClient.storeFile(filename, inputStream);
	        if (!success) {
	            throw new IOException("Failed to upload file: " + filename);
	        }
	    }

	   
	    ftpClient.logout();
	    ftpClient.disconnect();
	}
	
	

	private static void deleteFtpFile(String filename) throws SocketException, IOException, FTPException {
		com.enterprisedt.net.ftp.FTPClient fcget = createFtpConnectionEditJ("BOBRefundResponse");
		fcget.delete(filename);
		fcget.quit();
	}
	

    // -------------------------------
    // CSV Utilities
    // -------------------------------
    private static Map<Integer, Map<String, String>> parseCsvFirstRow(InputStream reqdata, String filename, List<String> tags) throws IOException {
    	
    	    Map<Integer, Map<String, String>> retmap = new LinkedHashMap<>();
    	    String fileExtension = filename.substring(filename.lastIndexOf('.') + 1).toLowerCase();
    	    System.out.println("fileExtension "+fileExtension);
    	    if (fileExtension.equals("csv")) {
    	    	
    	    	try (Reader reader = new InputStreamReader(reqdata);
    					CSVParser csvParser = CSVFormat.DEFAULT.builder().build().parse(reader)) {
    	    	   
    	        	    int rowIndex = 0;
    	        	    for (CSVRecord record : csvParser) {
    	        	        Map<String, String> datamap = new LinkedHashMap<>();
    	        	        for (int j = 0; j < tags.size(); j++) {
    	        	            String value = j < record.size() ? record.get(j) : "";
    	        	            datamap.put(tags.get(j), value.trim());
    	        	        }
    	        	        retmap.put(rowIndex++, datamap);
    	        	        break; //Exit after processing the first row
    	        	    }
    	        }
    	       		
    	    }else {
    	        throw new IllegalArgumentException("Unsupported file type: " + fileExtension);
    	    }

    	    return retmap;
    	//return parseCsv(is, filename, tags, true);
    }

    private static Map<Integer, Map<String, String>> parseCsvTransactions(InputStream reqdata, String filename, List<String> tags) throws IOException {
     
    	    Map<Integer, Map<String, String>> retmap = new LinkedHashMap<>();
    	    String fileExtension = filename.substring(filename.lastIndexOf('.') + 1).toLowerCase();
    	    System.out.println("fileExtension "+fileExtension);
    	    if (fileExtension.equals("csv")) {
    	    	
    	    	try (Reader reader = new InputStreamReader(reqdata);
    					CSVParser csvParser = CSVFormat.DEFAULT.builder().build().parse(reader)) {
    	    	   
    	        	    int rowIndex = 0;
    	        	    boolean isFirstRow = true;
    	        	    for (CSVRecord record : csvParser) {
    	        	    	 if (isFirstRow) {
    	        	             isFirstRow = false;
    	        	             continue; // Skip the first row
    	        	         }
    	        	    	
    	        	        Map<String, String> datamap = new LinkedHashMap<>();
    	        	        for (int j = 0; j < tags.size(); j++) {
    	        	            String value = j < record.size() ? record.get(j) : "";
    	        	            datamap.put(tags.get(j), value.trim());
    	        	        }
    	        	        retmap.put(rowIndex++, datamap);
    	        	    }
    	        }
    	       		
    	    }else {
    	        throw new IllegalArgumentException("Unsupported file type: " + fileExtension);
    	    }

    	    return retmap;
    	
    	// return parseCsv(is, filename, tags, false);
    }

    private static Map<Integer, Map<String, String>> parseCsv(InputStream is, String filename, List<String> tags, boolean firstRowOnly) throws IOException {
        Map<Integer, Map<String, String>> result = new LinkedHashMap<>();
        String ext = filename.substring(filename.lastIndexOf('.') + 1).toLowerCase();
        if (!"csv".equals(ext)) throw new IllegalArgumentException("Unsupported file: " + filename);

        try (Reader reader = new InputStreamReader(is);
             CSVParser parser = CSVFormat.DEFAULT.builder().build().parse(reader)) {

            int index = 0;
            for (CSVRecord record : parser) {
                Map<String, String> row = new LinkedHashMap<>();
                for (int i = 0; i < tags.size(); i++) {
                    row.put(tags.get(i), i < record.size() ? record.get(i).trim() : "");
                }
                result.put(index++, row);
                if (firstRowOnly) break;
            }
        }
        return result;
    }
    
    // -------------------------------
    // ACK related Methods
    // -------------------------------
    
    private static List<String> getAckFileTags() {
		List<String> taglist = new ArrayList<String>();
		String tags = "FILE_ID|File_corelation_ID|Customer_ID|Corporate File ID|File_Status|File_Status_Code|File_Status_Description";
		String tagsar[] = StringUtils.split(tags, "|");
		for (int i = 0; i < tagsar.length; i++) {
			String tag = tagsar[i];
			taglist.add(tag);
		}

		return taglist;
	}
    
	
	
    private static boolean updateACKData(String filename, Connection connection)
	{
    	boolean updated=false;
		int upcnt = 0;
		try {
		connection.setAutoCommit(false);
		String ack="ACK";
		String sqlup = "Update SCRAP.BANK_SCRAP_EMD_REFUND_REQUEST_RESPONSE SET RESPONSE_TYPE=? WHERE REQUEST_FILE_NAME=? ";
		PreparedStatement pstmt = connection.prepareStatement(sqlup);
		pstmt.setString(1, ack);
		pstmt.setString(2, filename);
		upcnt=pstmt.executeUpdate();
		pstmt.close();
		logInfo("upcnt "+upcnt);
		
		HashSet<String> refundIDset = new HashSet<String>();
		sqlup = "SELECT CUSTOMER_REF_NO FROM SCRAP.BANK_SCRAP_EMD_REFUND_REQUEST_RESPONSE WHERE REQUEST_FILE_NAME=? ";
		pstmt = connection.prepareStatement(sqlup);
		pstmt.setString(1, filename);
		ResultSet rstmt=pstmt.executeQuery();
		while(rstmt.next()){
			refundIDset.add(rstmt.getString(1));
		}
		rstmt.close();
		pstmt.close();
		
		HashMap<String,String> map=new HashMap<String,String>();
		for(String ref_id : refundIDset) {
			sqlup="SELECT BUYER_REF_ID FROM EMD_REFUND_REQUEST WHERE REF_ID=?";
			pstmt= connection.prepareStatement(sqlup);
			pstmt.setString(1,ref_id);
			rstmt=pstmt.executeQuery();
			if(rstmt.next()){
				map.put(ref_id, rstmt.getString(1));
			}
			rstmt.close();
			pstmt.close();
		}
			
		int upcntmap=0;
		if(!map.isEmpty()) {
		String tracedata="ACK file "+filename;
		sqlup="INSERT INTO TRACE_MASTER (USER,REGION,DONE,WHEN,BUYER_REF_ID,REMARKS,AUC_REF_ID) VALUES(?,?,?,CURRENT TIMESTAMP,?,?,?)";
		pstmt=connection.prepareStatement(sqlup);
		
		for (Map.Entry<String,String> mapElement : map.entrySet()) {
			String refund_id = mapElement.getKey();
			String buyer_id = mapElement.getValue();
			pstmt.setString(1,"GAGLPEMD AUTO REFUND");
			pstmt.setString(2,"system");
			pstmt.setString(3,"Successful ACK Updated");
			pstmt.setString(4,buyer_id);
			pstmt.setString(5,tracedata);
			pstmt.setString(6,refund_id);
			upcntmap=upcntmap+pstmt.executeUpdate();
			
		}
		
		pstmt.close();
		
		}
		logInfo("upcntmap "+upcntmap);
		if(upcnt>0 && (upcntmap==upcnt))
		{
			connection.commit();
			updated=true;
		}
		}catch (Exception ex) {
			System.out.println("Update ACK Exception-->" + ex);
			logError("Main processing ACK Data Exception", ex);
			try {
				connection.rollback();
			} catch (SQLException e) {
				e.printStackTrace();
				logError("Rollback failed after ACK update", e);
			}
		}
		
		return updated;
	}
    
    
	
    private static String extractFileName(Map<Integer, Map<String, String>> transactioData)
			throws  ParseException {
		
		String nackfileId = "";
		System.out.println("transactioData size "+transactioData.size());
		if (transactioData.size() > 0) { 
			for (int i = 0; i < transactioData.size(); i++) {
				Map<String, String> inwarddata = transactioData.get(i);
				System.out.println("inwarddata "+inwarddata);
				 nackfileId = StringUtils.trimToNull(inwarddata.get("FILE_ID"));//REFUND_REQUEST_ID
				
				
			}
			
		}
		return nackfileId;
	}
    
    private static String extractFileStatus(Map<Integer, Map<String, String>> transactioData)
			throws  ParseException {
		
		String fileStatus = "";
		System.out.println("transactioData size "+transactioData.size());
		if (transactioData.size() > 0) { 
			for (int i = 0; i < transactioData.size(); i++) {
				Map<String, String> inwarddata = transactioData.get(i);
				//System.out.println("inwarddata "+inwarddata);
				fileStatus = StringUtils.trimToNull(inwarddata.get("File_Status"));//REFUND_REQUEST_ID
				System.out.println("fileStatus "+fileStatus);
				
			}
			
		}
		return fileStatus;
	}
	
    // -------------------------------
    //NACK related Methods
    // -------------------------------
    
    private static List<String> getNackFileLevelTags() {
		List<String> taglist = new ArrayList<String>();
		String tags = "FILE_ID|File_corelation_ID|Customer_ID|File_ID|File_Status|File_Error_Code|File_Error_Description|File_Error_Code1|File_Error_Description1|File_Error_Code2|File_Error_Description2|File_Error_Code3|File_Error_Description3|File_Error_Code4|File_Error_Description4|No_of_TXN";		
		String tagsar[] = StringUtils.split(tags, "|");
		for (int i = 0; i < tagsar.length; i++) {
			String tag = tagsar[i];
			taglist.add(tag);
		}

		return taglist;
	}
	
    private static List<String> getNackTransactionLevelTags() {
		List<String> taglist = new ArrayList<String>();
		String tags = "TRN_Reference|Customer_TRN_Reference|IPS_TRN_Reference|TRN_Status|Error_Code|Error_Description|Error_code1|Error_code_Description1|Error_Code2|Error_Description2|Error_Code3|Error_Description3|Error_Code4|Error_Description4";
		String tagsar[] = StringUtils.split(tags, "|");
		for (int i = 0; i < tagsar.length; i++) {
			String tag = tagsar[i];
			taglist.add(tag);
		}

		return taglist;
	}
	
    private static Map<String, String> getCustRefData(Map<Integer, Map<String, String>> transactioData)
			throws  ParseException {
		
		Map<String, String> custMap = new HashMap<String, String>();
		String custId="";
		String Reject="";
		System.out.println("transactioData size "+transactioData.size());
		if (transactioData.size() > 0) { 
			for (int i = 0; i < transactioData.size(); i++) {
				Map<String, String> inwarddata = transactioData.get(i);
				System.out.println("inwarddata "+inwarddata);
				custId = StringUtils.trimToNull(inwarddata.get("Customer_TRN_Reference"));//REFUND_REQUEST_ID
				Reject = StringUtils.trimToNull(inwarddata.get("Error_Description"));//REFUND_REQUEST_ID
				//Reject = StringUtils.stripStart(StringUtils.trimToNull(inwarddata.get("TRN_Status")), "0");//REFUND_REQUEST_ID
				
				custMap.put(custId, Reject);
				
			}
			
		}
		return custMap;
	}
	
    private static int updateNACKData(Map<String, String> transactiondata, String requestfilename, Connection connection) {
        int upcnt = 0;
        int upcnt1 = 0;
        String nack = "NACK";

        try {
            connection.setAutoCommit(false);

            // First update: set RESPONSE_TYPE
            String sqlUpdateResponseType = "UPDATE BANK_SCRAP_EMD_REFUND_REQUEST_RESPONSE SET RESPONSE_TYPE=? WHERE REQUEST_FILE_NAME=?";
            try (PreparedStatement pstmt = connection.prepareStatement(sqlUpdateResponseType)) {
                pstmt.setString(1, nack);
                pstmt.setString(2, requestfilename);
                upcnt += pstmt.executeUpdate();
            }

            System.out.println("Initial update count (RESPONSE_TYPE): " + upcnt);
            System.out.println("requestfilename " + requestfilename);
          //if there is any tansaction level data then update the field of those refund id 
            // Second update: batch update REJECT_REASON for each customer
           
            // Commit only if any update occurred
            
            
            if (!transactiondata.isEmpty()) {
                System.out.println("transactiondata: " + transactiondata);

                try (Statement stmt = connection.createStatement()) {

                    for (Map.Entry<String, String> entry : transactiondata.entrySet()) {
                        System.out.println("Value: " + entry.getValue());
                        System.out.println("Key: " + entry.getKey());
                        System.out.println("Request filename: " + requestfilename);

                        int key = Integer.parseInt(entry.getKey());
                        String rejectReason = entry.getValue().replace("'", "''");   // Escape single quotes
                        String requestFile = requestfilename.replace("'", "''");    // Escape single quotes

                        String sql = String.format(
                            "UPDATE BANK_SCRAP_EMD_REFUND_REQUEST_RESPONSE SET REJECT_REASON='%s' WHERE CUSTOMER_REF_NO=%d AND REQUEST_FILE_NAME='%s'",
                            rejectReason, key, requestFile);

                        stmt.addBatch(sql);
                    }

                    int[] batchResults = stmt.executeBatch();
                    
                    for (int count : batchResults) {
                        System.out.println("Update count: " + count);
                        if (count >= 0) {
                        	upcnt1 += count;
                        }
                    }

                    System.out.println("Batch update executed. Number of statements: " + batchResults.length);
                    System.out.println("Total REJECT_REASON updates applied: " + upcnt1);
                }
            }

            System.out.println("upcnt "+upcnt);
            System.out.println("upcnt1 "+upcnt1);
            
            if (upcnt > 0 || upcnt1 > 0) {
                connection.commit();
                System.out.println("Transaction committed successfully.");
            } else {
                System.out.println("No updates to commit.");
            }

        } catch (Exception ex) {
            logError("Exception during NACK data update", ex);
            try {
                connection.rollback();
                System.out.println("Transaction rolled back due to exception.");
            } catch (SQLException rollbackEx) {
                logError("Rollback failed after NACK data update", rollbackEx);
            }
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException ex) {
                logError("Failed to reset auto-commit to true", ex);
            }
        }

        return upcnt + upcnt1;  // Return total number of updated rows
    }


		
   
    private static int updateNACKDataWalletNew(String requestFilename, Connection connection) {
    	
    	
    	final String DEFAULT_EMAIL = "admin@mstcecommerce.com";
        int updateCount = 0;
        
        String sqlup="SELECT REF_ID,COALESCE(PROJNAME,'') FROM EMD_REFUND_REQUEST WHERE REF_ID=?";
        
        String selectCustomerSql = "SELECT CUSTOMER_REF_NO FROM BANK_SCRAP_EMD_REFUND_REQUEST_RESPONSE WHERE REQUEST_FILE_NAME = ?";

        String selectRefundSql = "Select br.PAY_AMT, br.Value_Date ,  COALESCE(br.REJECT_REASON,'NACK Received from BOB'), br.Bene_Acc_No, efr.buyer_ref_id FROM BANK_SCRAP_EMD_REFUND_REQUEST_RESPONSE br , EMD_REFUND_REQUEST efr WHERE br.CUSTOMER_REF_NO=efr.REF_ID AND efr.REF_ID=? AND br.REQUEST_FILE_NAME =?";

        String selectBuyerSql = "SELECT BANKER_NAME, COALESCE(email, ?) FROM BUYER_MASTER WHERE BUYER_REF_ID = ?";

        String updateMpqSql = "UPDATE SELLER_MPQU SET MPQ = MPQ + ?, TREFUND = TREFUND - ? WHERE BUYER_REF_ID = ? AND SELLER_REF_ID = ?";

        String updateMonthlyMpqSql = "UPDATE BUYER_MONTHLY_MPQ_STATUS SET TREFUND = (TREFUND - ?) WHERE BUYER_REF_ID = ? AND MONTH1 = MONTH(CURRENT_TIMESTAMP) AND YEAR1 = YEAR(CURRENT_TIMESTAMP) AND SELLER_REF_ID = ?";

        String insertEmdMasterSql = "INSERT INTO EMD_MASTER ( ADMIN_USER_ID, ENTRY_TIME, SELLER_REF_ID, BUYER_REF_ID, DD_NO, DD_DATE, BANK, AMT, MPQ_STATUS, REFUND_DATE, BANK_STATUS, DD_NO_INT, DEPOSITED_AT, REMARKS, BANK_CHALLAN) VALUES (?, CURRENT TIMESTAMP, ?, ?, ?, ?, ?, ?, 'R', ?, 'F', ?, ?, ?, ?)";

        String insertTraceSql = "INSERT INTO TRACE_MASTER (USER, REGION, DONE, DOC_NO, WHEN, BUYER_REF_ID, REMARKS, AUC_REF_ID) VALUES (?, ?, ?, ?, CURRENT TIMESTAMP, ?, ?, ?)";

        String updateRefundStatusSql = " UPDATE EMD_REFUND_REQUEST SET STATUS = 'F' WHERE BUYER_REF_ID = ? AND REF_ID = ? AND STATUS = 'U'";

        String updateNPARefund="UPDATE NPA_EMD_REFUND_PROPERTY SET STATUS='F' WHERE BUYER_REF_ID=? AND EMD_REFUND_REF_ID=? ";
        
        try {
            connection.setAutoCommit(false);

            // Step 1: Get customer reference numbers
            Set<String> customerRefs = new HashSet<>();
            try (PreparedStatement ps = connection.prepareStatement(selectCustomerSql)) {
                ps.setString(1, requestFilename);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        customerRefs.add(rs.getString(1));
                    }
                }
            }
            System.out.println("customerRefs "+customerRefs);
            
            
            

            // Prepare batch statements
            try (PreparedStatement psUpdateMpq = connection.prepareStatement(updateMpqSql);
                 PreparedStatement psUpdateMonthlyMpq = connection.prepareStatement(updateMonthlyMpqSql);
                 PreparedStatement psInsertEmd = connection.prepareStatement(insertEmdMasterSql);
                 PreparedStatement psInsertTrace = connection.prepareStatement(insertTraceSql);
                 PreparedStatement psUpdateRefundStatus = connection.prepareStatement(updateRefundStatusSql);
            	 PreparedStatement psUpdateNPARefund= connection.prepareStatement(updateNPARefund)) {
            	
            	int data_cnt_batch=0;
                for (String refundId : customerRefs) {
                	 System.out.println("refundId "+refundId);
                    try (PreparedStatement psRefund = connection.prepareStatement(selectRefundSql)) {
                        psRefund.setString(1, refundId);
                        psRefund.setString(2, requestFilename);
                        try (ResultSet rsRefund = psRefund.executeQuery()) {
                            while (rsRefund.next()) {
                                String refundAmt = rsRefund.getString(1);
                                String refundDate = rsRefund.getString(2);
                                String refundUtr = rsRefund.getString(3); // REJECT_REASON
                                if(refundUtr.length()>81) {
                                refundUtr=refundUtr.substring(0, 80); //DD_NO length is 100 octets so allowing upto 80 and appending refunid 
                                }
                                String refundBankAcNo = rsRefund.getString(4);
                                String buyerId = rsRefund.getString(5);
                                refundUtr=refundUtr+" refundId: "+refundId;
                                
                                System.out.println("refundUtr: " + refundUtr);
                                
                                //get desired seller_ref_id
                                String projname=""; String Seller_Ref_Id="2";String tracedata="";String emdMasterRemarks="GAGLPEMD ONLINE REFUND";String globalUserId="glpemdrefund";//PTS 17077
                                
                                try (PreparedStatement ps = connection.prepareStatement(sqlup)) {
                                    ps.setString(1, refundId);
                                    try (ResultSet rs = ps.executeQuery()) {
                                        if (rs.next()) {
                                        	projname=rs.getString(2);//PTS 17077
                                			projname=projname.trim();//PTS 17077
                                			if(projname.equalsIgnoreCase("NPA")){//PTS 17077
                                				Seller_Ref_Id="17";
                                				emdMasterRemarks = "NPAPEMD ONLINE REFUND";
                                				globalUserId="npapemdrefund";
                                			tracedata=" Project Name "+projname+" SELLER_REF_ID 2 Changed to "+Seller_Ref_Id;
                                			}//PTS 17077
                                        }
                                    }
                                }
                                System.out.println("Seller_Ref_Id: " + Seller_Ref_Id);
                                System.out.println("emdMasterRemarks: " + emdMasterRemarks);
                                System.out.println("projname: " + projname);
                                
                                
                         

                                // Get buyer bank
                                String refundBank;
                                try (PreparedStatement psBuyer = connection.prepareStatement(selectBuyerSql)) {
                                    psBuyer.setString(1, DEFAULT_EMAIL);
                                    psBuyer.setString(2, buyerId);
                                    try (ResultSet rsBuyer = psBuyer.executeQuery()) {
                                        refundBank = rsBuyer.next() ? rsBuyer.getString(1) : null;
                                    }
                                }

                                tracedata +="refund_id="+refundId+":bidder_id="+buyerId+":refund_amt="+refundAmt+":refund_date="+refundDate+":refund_utr="+refundUtr+":refund_bank="+refundBank;
                                System.out.println("Trace data: " + tracedata);

                                double refundAmtVal = refundAmt != null && !refundAmt.isEmpty()
                                        ? Double.parseDouble(refundAmt)
                                        : 0.0;

                                System.out.println("globalUserId: " + globalUserId);
                                System.out.println("Seller_Ref_Id: " + Seller_Ref_Id);
                                System.out.println("buyerId: " + buyerId);
                                System.out.println("refundUtr: " + refundUtr);
                                System.out.println("refundDate: " + refundDate);
                                System.out.println("refundBank: " + refundBank);
                                System.out.println("refundAmtVal: " + refundAmtVal);
                                System.out.println("refundId: " + refundId);
                                System.out.println("refundBankAcNo: " + refundBankAcNo);
                                System.out.println("emdMasterRemarks: " + emdMasterRemarks);
                                
                                
                                // Batch: Update SELLER_MPQU
                                psUpdateMpq.setDouble(1, refundAmtVal);
                                psUpdateMpq.setDouble(2, refundAmtVal);
                                psUpdateMpq.setString(3, buyerId);
                                psUpdateMpq.setString(4, Seller_Ref_Id);
                                psUpdateMpq.addBatch();
                                System.out.println("SELLER_MPQU: " );

                                // Batch: Update monthly MPQ status
                                psUpdateMonthlyMpq.setDouble(1, refundAmtVal);
                                psUpdateMonthlyMpq.setString(2, buyerId);
                                psUpdateMonthlyMpq.setString(3, Seller_Ref_Id);
                                psUpdateMonthlyMpq.addBatch();
                                System.out.println(" Update monthly MPQ status " );

                                // Batch: Insert into EMD_MASTER
                                
                                psInsertEmd.setString(1, globalUserId);
                                psInsertEmd.setString(2, Seller_Ref_Id);
                                psInsertEmd.setString(3, buyerId);
                                psInsertEmd.setString(4, refundUtr);
                                psInsertEmd.setString(5, refundDate);
                                psInsertEmd.setString(6, refundBank);
                                psInsertEmd.setDouble(7, -refundAmtVal);
                                psInsertEmd.setString(8, refundDate);
                                psInsertEmd.setString(9, refundId);
                                psInsertEmd.setString(10, refundBankAcNo);
                                psInsertEmd.setString(11, emdMasterRemarks+" FAILED");
                                psInsertEmd.setString(12, "BOB");
                                psInsertEmd.addBatch();
                                System.out.println("EMD_MASTER " );

                                // Batch: Insert trace
                                psInsertTrace.setString(1, "GAGLPEMD AUTO REFUND");
                                psInsertTrace.setString(2, "system");
                                psInsertTrace.setString(3, "Failed Pre Bid EMD Refund");
                                psInsertTrace.setString(4, "Amount=" + refundAmt);
                                psInsertTrace.setString(5, buyerId);
                                psInsertTrace.setString(6, tracedata);
                                psInsertTrace.setString(7, refundId);
                                psInsertTrace.addBatch();
                                System.out.println("trace " );

                                // Batch: Update refund request status
                                psUpdateRefundStatus.setString(1, buyerId);
                                psUpdateRefundStatus.setString(2, refundId);
                                psUpdateRefundStatus.addBatch();
                                System.out.println("Update refund request status " );
                                
                              //PTS 17077
                              
                    			psUpdateNPARefund.setString(1,buyerId);
                    			psUpdateNPARefund.setString(2,refundId);
                    			psUpdateNPARefund.addBatch();
                    			System.out.println("Update NPA " );
                                
                                data_cnt_batch++;
                            }
                        }
                    }
                }
                int batch_updtMPQ_cnt = 0;
                int[] updtMPQ = psUpdateMpq.executeBatch();
    			System.out.println("updtMPQ "+updtMPQ.length);
    			
    			for (int d : updtMPQ) {
    				if(d >= 0 || d==psUpdateMpq.SUCCESS_NO_INFO){
    					batch_updtMPQ_cnt += d;
    				}
    			}
    			System.out.println("batch_updtMPQ_cnt "+batch_updtMPQ_cnt);
    			boolean updtMPQ_flag = checkBatchUpdateCounts(updtMPQ);
    			System.out.println("updtMPQ_flag "+updtMPQ_flag);
    			
    			int batch_UpdateMonthlyMpq_cnt = 0;
                int[] UpdateMonthlyMpq = psUpdateMonthlyMpq.executeBatch();
    			System.out.println("UpdateMonthlyMpq "+UpdateMonthlyMpq.length);
    			
    			for (int d : UpdateMonthlyMpq) {
    				if(d >= 0 || d==psUpdateMonthlyMpq.SUCCESS_NO_INFO){
    					batch_UpdateMonthlyMpq_cnt += d;
    				}
    			}
    			System.out.println("batch_UpdateMonthlyMpq_cnt "+batch_UpdateMonthlyMpq_cnt);
    			boolean UpdateMonthlyMpq_flag = checkBatchUpdateCounts(UpdateMonthlyMpq);
    			System.out.println("UpdateMonthlyMpq_flag "+UpdateMonthlyMpq_flag);
    			
    			int batch_InsertEmd_cnt = 0;
                int[] InsertEmd = psInsertEmd.executeBatch();
    			System.out.println("InsertEmd "+InsertEmd.length);
    			
    			for (int d : InsertEmd) {
    				if(d >= 0 || d==psInsertEmd.SUCCESS_NO_INFO){
    					batch_InsertEmd_cnt += d;
    				}
    			}
    			System.out.println("batch_InsertEmd_cnt "+batch_InsertEmd_cnt);
    			boolean InsertEmd_flag = checkBatchUpdateCounts(InsertEmd);
    			System.out.println("InsertEmd_flag "+InsertEmd_flag);
    			
    			int batch_InsertTrace_cnt = 0;
                int[] InsertTrace = psInsertTrace.executeBatch();
    			System.out.println("InsertTrace "+InsertTrace.length);
    			
    			for (int d : InsertTrace) {
    				if(d >= 0 || d==psInsertTrace.SUCCESS_NO_INFO){
    					batch_InsertTrace_cnt += d;
    				}
    			}
    			System.out.println("batch_InsertTrace_cnt "+batch_InsertTrace_cnt);
    			boolean InsertTrace_flag = checkBatchUpdateCounts(InsertTrace);
    			System.out.println("InsertTrace_flag "+InsertTrace_flag);
    			
    			int batch_UpdateRefundStatus_cnt = 0;
                int[] UpdateRefundStatus = psUpdateRefundStatus.executeBatch();
    			System.out.println("UpdateRefundStatus "+UpdateRefundStatus.length);
    			
    			for (int d : UpdateRefundStatus) {
    				if(d >= 0 || d==psUpdateRefundStatus.SUCCESS_NO_INFO){
    					batch_UpdateRefundStatus_cnt += d;
    				}
    			}
    			System.out.println("batch_UpdateRefundStatus_cnt "+batch_UpdateRefundStatus_cnt);
    			boolean UpdateRefundStatus_flag = checkBatchUpdateCounts(UpdateRefundStatus);
    			System.out.println("UpdateRefundStatus_flag "+UpdateRefundStatus_flag);
    			
    			
    			int batch_UpdateNPARefund_cnt = 0;
                int[] UpdateNPARefund = psUpdateNPARefund.executeBatch();
    			System.out.println("UpdateNPARefund "+UpdateNPARefund.length);
    			
    			for (int d : UpdateRefundStatus) {
    				if(d >= 0 || d==psUpdateRefundStatus.SUCCESS_NO_INFO){
    					batch_UpdateNPARefund_cnt += d;
    				}
    			}
    			System.out.println("batch_UpdateNPARefund_cnt "+batch_UpdateNPARefund_cnt);
    			
    			
    			System.out.println("data_cnt_batch "+data_cnt_batch);
    			if(updtMPQ_flag && UpdateMonthlyMpq_flag && InsertEmd_flag && InsertTrace_flag && UpdateRefundStatus_flag && data_cnt_batch>0 && batch_updtMPQ_cnt>0 && batch_UpdateMonthlyMpq_cnt>0 && batch_InsertEmd_cnt>0 && batch_InsertTrace_cnt>0 && batch_UpdateRefundStatus_cnt>0) 
    			{
    				System.out.println("commit successful");
    				updateCount=1;
    				connection.commit();
    			}
    			
                
                
                /*boolean allSuccess =
                	    executeBatchAndValidate(psUpdateMpq, data_cnt_batch, "UpdateMpq") &&
                	    executeBatchAndValidate(psUpdateMonthlyMpq, data_cnt_batch, "UpdateMonthlyMpq") &&
                	    executeBatchAndValidate(psInsertEmd, data_cnt_batch, "InsertEmd") &&
                	    executeBatchAndValidate(psInsertTrace, data_cnt_batch, "InsertTrace") &&
                	    executeBatchAndValidate(psUpdateRefundStatus, data_cnt_batch, "UpdateRefundStatus");

                System.out.println("allSuccess "+allSuccess);
                	if (allSuccess) {
                	    updateCount = 1;
                	    connection.commit();
                	}
            */

            }

           
        } catch (Exception ex) {
        	updateCount=0;
            logError("Main processing NACK Wallet Exception", ex);
            try {
                connection.rollback();
            } catch (SQLException e) {
                logError("Rollback failed after NACK Wallet update", e);
            }
        }

        return updateCount;
    }


	

    // -------------------------------
    // Logging Helpers
    // -------------------------------
    private static void logInfo(String msg) {
        System.out.println("[INFO] " + msg);
    }

    private static void logError(String msg, Exception e) {
        System.out.println("[ERROR] " + msg +" Exception "+e.getMessage());
        e.printStackTrace();
    }
}
