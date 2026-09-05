package com;

import com.enterprisedt.net.ftp.FTPClient;
import com.enterprisedt.net.ftp.FTPException;
import java.io.*;
import java.util.*;
import java.sql.*;
import javax.servlet.http.HttpSession;
import java.net.URL;
import java.io.*;
import java.util.Map.Entry;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.InputStream;
import java.util.Calendar;
import java.security.Principal;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.itextpdf.text.pdf.draw.LineSeparator;
import com.jcraft.jsch.*;

public class TestSFTP {
	public static void main(String[] args) {
		String remoteFilePath = "mlcl/result/";
		String FILENAME = "MLCL_MAIL_ATTCAHMENT.pdf";
		boolean fileuploaded = false;
		byte[] fileContent = null;
		try {
			Font H2_FONT   = new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD);
			Document document = new Document(); // left, right, top, bottom margins
			ByteArrayOutputStream byt = new ByteArrayOutputStream();
			PdfWriter writer = PdfWriter.getInstance(document, byt);
			writer.setBoxSize("art", new Rectangle(500, 800));

			document.open();
			 Paragraph deptPara = new Paragraph("Here Inside PDF", H2_FONT);
	        deptPara.setAlignment(Element.ALIGN_CENTER);
	        document.add(deptPara);
			document.close();

			JSch jsch = new JSch();
			Session session1 = null;
			session1 = jsch.getSession("wasduser", "10.1.5.30", 22);
			session1.setConfig("StrictHostKeyChecking", "no");
			session1.setPassword("wasd@123");
			session1.connect();
			fileContent = byt.toByteArray();
			System.out.println("fileContent :" + fileContent);
			ByteArrayInputStream is = new ByteArrayInputStream(fileContent);
			Channel channel = session1.openChannel("sftp");
			channel.connect();
			ChannelSftp sftpChannel = (ChannelSftp) channel;
			sftpChannel.cd(remoteFilePath);
			sftpChannel.put(is, FILENAME);
			long fileSize = sftpChannel.lstat(FILENAME).getSize();
			int bytsize = byt.size();
			System.out.println("fileSize :" + fileSize);
			System.out.println("bytsize :" + bytsize);
			if (fileSize == bytsize && bytsize != 0)
				fileuploaded = true;
			else
				fileuploaded = false;
			sftpChannel.exit();
			channel.disconnect();
			session1.disconnect();
		} catch (JSchException e) {
			System.out.println("JSchException " + e);
			fileuploaded = false;
			e.printStackTrace();
		} catch (SftpException e) {
			System.out.println("SftpException " + e);
			fileuploaded = false;
			e.printStackTrace();
		}catch (Exception ex) {
			System.out.println("Exception " + ex);
		}
	}
}
