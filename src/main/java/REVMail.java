//package revauc;


import java.io.*;
import java.net.Socket;
import java.util.Date;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

// Referenced classes of package revauc:
//            REVMailException

public class REVMail
    implements Serializable
{

    private Socket smtp;
    private BufferedReader input;
    private PrintStream output;
    private String smtpServer;
    private String serverReply;
    private int port;
    Properties props;
    Session sendMailSession;
    Store store;
    Transport transport;

    public REVMail()
    {
        smtpServer = "";
        port = 25;
        props = new Properties();
    }

    public void sendMail(String mailfrom, String mailto, String subject, String message)
        throws REVMailException
    {
        connect();
        hail(mailfrom, mailto);
        sendMessage(mailfrom, mailto, subject, message);
        logout();
    }
	
	
    public void sendMailWithName(String mailfrom, String mailto, String subject, String message, String fromName)
        throws REVMailException
    {
        connect();
        hail(mailfrom, mailto);
        sendMessageWithName(mailfrom, mailto, subject, message, fromName);
        logout();
    }

    public void sendMailWithCC(String mailfrom, String mailto, String mailcc, String subject, String message)
        throws REVMailException
    {
        connect();
        hail(mailfrom, mailto);
        sendMessage(mailfrom, mailto, mailcc, subject, message);
        logout();
    }

	public void sendMailWithCCWithName(String mailfrom, String mailto, String mailcc, String subject, String message, String fromName)
        throws REVMailException
    {
        connect();
        hail(mailfrom, mailto);
        sendMessageWithName(mailfrom, mailto, mailcc, subject, message, fromName);
        logout();
    }

	
    public void sendMailWithMultiCC(String mailfrom, String mailto, String mailcc1[], String subject, String message)
        throws REVMailException
    {
        connect();
        hail(mailfrom, mailto);
        sendMessage(mailfrom, mailto, mailcc1, subject, message);
        logout();
    }
	
	public void sendMailWithMultiCCWithName(String mailfrom, String mailto, String mailcc1[], String subject, String message, String fromName)
        throws REVMailException
    {
        connect();
        hail(mailfrom, mailto);
        sendMessageWithName(mailfrom, mailto, mailcc1, subject, message, fromName);
        logout();
    }
	
	public void sendMailWithMultiTo(String mailfrom, String mailto, String mailcc1[], String subject, String message, String fromName)
        throws REVMailException
    {
        connect();
        hail(mailfrom, mailto);
        sendMessageMultipleTo(mailfrom, mailto, mailcc1, subject, message);
        logout();
    }
	
	public void sendMailWithMultiToWithName(String mailfrom, String mailto, String mailcc1[], String subject, String message, String fromName)
        throws REVMailException
    {
        connect();
        hail(mailfrom, mailto);
        sendMessageMultipleToWithName(mailfrom, mailto, mailcc1, subject, message, fromName);
        logout();
    }
	
	 public void sendMailWithMultiCCWithAttachmentWithName(String mailfrom, String mailto, String mailcc1[], String subject, String message, byte[] fileData,String fromName,String fileName)
		        throws REVMailException
    {
        connect();
        hail(mailfrom, mailto);
        sendMessageWithAttachment(mailfrom, mailto, mailcc1, subject, message,fileData, fromName,fileName);
        logout();
    }
	 
	public void sendMailWithMultiToWithNameWithBCC(String mailfrom, String mailto, String mailcc1[], String mailbcc, String subject, String message, String fromName) throws REVMailException{
			connect();
			hail(mailfrom, mailto);
			sendMessageMultipleToWithNameWithBCC(mailfrom, mailto, mailcc1, mailbcc, subject, message, fromName);
			logout();
	}

    public void connect()
        throws REVMailException
    {
        try
        {
            sendMailSession = Session.getInstance(props, null);
            props.put("mail.smtp.host", smtpServer);
        }
        catch(Exception e)
        {
            throw new REVMailException(e.getMessage());
        }
    }

    public void hail(String s2, String s3)
        throws REVMailException
    {
    }

    public void sendMessage(String mailfrom, String mailto, String subject, String message)
        throws REVMailException
    {
        Date lgmt_date = new Date(System.currentTimeMillis());
        try
        {
            Message newMessage = new MimeMessage(sendMailSession);
            newMessage.setFrom(new InternetAddress(mailfrom));
            newMessage.setRecipient(javax.mail.Message.RecipientType.TO, new InternetAddress(mailto));
            newMessage.setSubject(subject);
            newMessage.setSentDate(lgmt_date);
            String EmailContent = "<html><head><title></title></head><body bgcolor=\"#FFFFFF\">";
            EmailContent = EmailContent + message;
            EmailContent = EmailContent + "</body></html>";
            newMessage.setContent(EmailContent, "text/html");
            transport = sendMailSession.getTransport("smtp");
            Transport.send(newMessage);
        }
        catch(MessagingException m)
        {
            System.out.println(m.toString());
        }
    }
	
    public void sendMessageWithName(String mailfrom, String mailto, String subject, String message, String fromName)
        throws REVMailException
    {
        Date lgmt_date = new Date(System.currentTimeMillis());
        try
        {
            Message newMessage = new MimeMessage(sendMailSession);
            newMessage.setFrom(new InternetAddress(mailfrom, fromName));
            newMessage.setRecipient(javax.mail.Message.RecipientType.TO, new InternetAddress(mailto));
            newMessage.setSubject(subject);
            newMessage.setSentDate(lgmt_date);
            String EmailContent = "<html><head><title></title></head><body bgcolor=\"#FFFFFF\">";
            EmailContent = EmailContent + message;
            EmailContent = EmailContent + "</body></html>";
            newMessage.setContent(EmailContent, "text/html");
            transport = sendMailSession.getTransport("smtp");
            Transport.send(newMessage);
        }
        catch(Exception m)
        {
            System.out.println(m.toString());
        }
    }

    public void sendMessage(String mailfrom, String mailto, String mailcc, String subject, String message)
        throws REVMailException
    {
        Date lgmt_date = new Date(System.currentTimeMillis());
        try
        {
            Message newMessage = new MimeMessage(sendMailSession);
            newMessage.setFrom(new InternetAddress(mailfrom));
            newMessage.setRecipient(javax.mail.Message.RecipientType.TO, new InternetAddress(mailto));
            newMessage.setRecipient(javax.mail.Message.RecipientType.CC, new InternetAddress(mailcc));
            newMessage.setSubject(subject);
            newMessage.setSentDate(lgmt_date);
            String EmailContent = "<html><head><title></title></head><body bgcolor=\"#FFFFFF\">";
            EmailContent = EmailContent + message;
            EmailContent = EmailContent + "</body></html>";
            newMessage.setContent(EmailContent, "text/html");
            transport = sendMailSession.getTransport("smtp");
            Transport.send(newMessage);
        }
        catch(MessagingException m)
        {
            System.out.println(m.toString());
        }
    }
	
    public void sendMessageWithName(String mailfrom, String mailto, String mailcc, String subject, String message, String fromName)
        throws REVMailException
    {
        Date lgmt_date = new Date(System.currentTimeMillis());
        try
        {
            Message newMessage = new MimeMessage(sendMailSession);
            newMessage.setFrom(new InternetAddress(mailfrom, fromName));
            newMessage.setRecipient(javax.mail.Message.RecipientType.TO, new InternetAddress(mailto));
            newMessage.setRecipient(javax.mail.Message.RecipientType.CC, new InternetAddress(mailcc));
            newMessage.setSubject(subject);
            newMessage.setSentDate(lgmt_date);
            String EmailContent = "<html><head><title></title></head><body bgcolor=\"#FFFFFF\">";
            EmailContent = EmailContent + message;
            EmailContent = EmailContent + "</body></html>";
            newMessage.setContent(EmailContent, "text/html");
            transport = sendMailSession.getTransport("smtp");
            Transport.send(newMessage);
        }
        catch(Exception m)
        {
            System.out.println(m.toString());
        }
    }

    public void sendMessage(String mailfrom, String mailto, String mailcc[], String subject, String message)
        throws REVMailException
    {
        Date lgmt_date = new Date(System.currentTimeMillis());
        try
        {
            Message newMessage = new MimeMessage(sendMailSession);
            newMessage.setFrom(new InternetAddress(mailfrom));
            newMessage.setRecipient(javax.mail.Message.RecipientType.TO, new InternetAddress(mailto));
            InternetAddress ia[] = new InternetAddress[mailcc.length];
            for(int x = 0; x < mailcc.length; x++)
            {
                ia[x] = new InternetAddress(mailcc[x]);
            }

            newMessage.setRecipients(javax.mail.Message.RecipientType.CC, ia);
            newMessage.setSubject(subject);
            newMessage.setSentDate(lgmt_date);
            String EmailContent = "<html><head><title></title></head><body bgcolor=\"#FFFFFF\">";
            EmailContent = EmailContent + message;
            EmailContent = EmailContent + "</body></html>";
            newMessage.setContent(EmailContent, "text/html");
            transport = sendMailSession.getTransport("smtp");
            Transport.send(newMessage);
        }
        catch(MessagingException m)
        {
            System.out.println(m.toString());
        }
    }
	
	public void sendMessageWithName(String mailfrom, String mailto, String mailcc[], String subject, String message, String fromName)
        throws REVMailException
    {
        Date lgmt_date = new Date(System.currentTimeMillis());
        try
        {
            Message newMessage = new MimeMessage(sendMailSession);
            newMessage.setFrom(new InternetAddress(mailfrom, fromName));
            newMessage.setRecipient(javax.mail.Message.RecipientType.TO, new InternetAddress(mailto));
            InternetAddress ia[] = new InternetAddress[mailcc.length];
            for(int x = 0; x < mailcc.length; x++)
            {
                ia[x] = new InternetAddress(mailcc[x]);
            }

            newMessage.setRecipients(javax.mail.Message.RecipientType.CC, ia);
            newMessage.setSubject(subject);
            newMessage.setSentDate(lgmt_date);
            String EmailContent = "<html><head><title></title></head><body bgcolor=\"#FFFFFF\">";
            EmailContent = EmailContent + message;
            EmailContent = EmailContent + "</body></html>";
            newMessage.setContent(EmailContent, "text/html");
            transport = sendMailSession.getTransport("smtp");
            Transport.send(newMessage);
        }
        catch(Exception m)
        {
            System.out.println(m.toString());
        }
    }
	
	public void sendMessageMultipleTo(String mailfrom, String mailto, String mailcc[], String subject, String message)
        throws REVMailException
    {
		String recipientto = mailto;
        Date lgmt_date = new Date(System.currentTimeMillis());
        try
        {
			String[] recipientList = recipientto.split(",");
			InternetAddress[] recipientAddress = new InternetAddress[recipientList.length];
			int counter = 0;
			for (String recipient : recipientList) {
				recipientAddress[counter] = new InternetAddress(recipient.trim());
				counter++;
			}
			
            Message newMessage = new MimeMessage(sendMailSession);
            newMessage.setFrom(new InternetAddress(mailfrom));
            newMessage.setRecipients(javax.mail.Message.RecipientType.TO,recipientAddress);
            InternetAddress ia[] = new InternetAddress[mailcc.length];
            for(int x = 0; x < mailcc.length; x++)
            {
                ia[x] = new InternetAddress(mailcc[x]);
            }

            newMessage.setRecipients(javax.mail.Message.RecipientType.CC, ia);
            newMessage.setSubject(subject);
            newMessage.setSentDate(lgmt_date);
            String EmailContent = "<html><head><title></title></head><body bgcolor=\"#FFFFFF\">";
            EmailContent = EmailContent + message;
            EmailContent = EmailContent + "</body></html>";
            newMessage.setContent(EmailContent, "text/html");
            transport = sendMailSession.getTransport("smtp");
            Transport.send(newMessage);
        }
        catch(MessagingException m)
        {
            System.out.println(m.toString());
        }
    }
	
	
	public void sendMessageMultipleToWithName(String mailfrom, String mailto, String mailcc[], String subject, String message, String fromName)
        throws REVMailException
    {
		
		String recipientto = mailto;
		
		
        Date lgmt_date = new Date(System.currentTimeMillis());
        try
        {
			String[] recipientList = recipientto.split(",");
			InternetAddress[] recipientAddress = new InternetAddress[recipientList.length];
			int counter = 0;
			for (String recipient : recipientList) {
				recipientAddress[counter] = new InternetAddress(recipient.trim());
				counter++;
			}
			
            Message newMessage = new MimeMessage(sendMailSession);
            newMessage.setFrom(new InternetAddress(mailfrom, fromName));
            newMessage.setRecipients(javax.mail.Message.RecipientType.TO, recipientAddress);
            InternetAddress ia[] = new InternetAddress[mailcc.length];
            for(int x = 0; x < mailcc.length; x++)
            {
                ia[x] = new InternetAddress(mailcc[x]);
            }

            newMessage.setRecipients(javax.mail.Message.RecipientType.CC, ia);
            newMessage.setSubject(subject);
            newMessage.setSentDate(lgmt_date);
            String EmailContent = "<html><head><title></title></head><body bgcolor=\"#FFFFFF\">";
            EmailContent = EmailContent + message;
            EmailContent = EmailContent + "</body></html>";
            newMessage.setContent(EmailContent, "text/html");
            transport = sendMailSession.getTransport("smtp");
            Transport.send(newMessage);
        }
        catch(Exception m)
        {
            System.out.println(m.toString());
        }
    }
	 public void sendMessageWithAttachment(String s, String s1, String as[], String s2, String s3, byte[] s4,String fromname,String filename)
		        throws REVMailException
		    {
		        Date date = new Date(System.currentTimeMillis());
		        try
		        {
		        	File tempFile = File.createTempFile("MSTC", "MSTC");
		            MimeMessage mimemessage = new MimeMessage(sendMailSession);
		            mimemessage.setFrom(new InternetAddress(s,fromname));
		            mimemessage.setRecipient(javax.mail.Message.RecipientType.TO, new InternetAddress(s1));
		            InternetAddress ainternetaddress[] = new InternetAddress[as.length];
		            for(int i = 0; i < as.length; i++)
		            {
		                ainternetaddress[i] = new InternetAddress(as[i]);
		            }

		            mimemessage.setRecipients(javax.mail.Message.RecipientType.CC, ainternetaddress);
		            mimemessage.setSubject(s2);
		            mimemessage.setSentDate(date);
		            String s5 = "<html><head><title>MSTC</title></head><body bgcolor=\"#FFFFFF\">";
		            s5 = s5 + s3;
		            s5 = s5 + "</body></html>";
		            MimeBodyPart mimebodypart = new MimeBodyPart();
		            mimebodypart.setContent(s5, "text/html");
		            MimeMultipart mimemultipart = new MimeMultipart();
		            mimemultipart.addBodyPart(mimebodypart);
		            mimebodypart = new MimeBodyPart();
		            try
		            {
		            	
		            	FileOutputStream fos = new FileOutputStream(tempFile);
		            	fos.write(s4);
		                FileDataSource filedatasource = new FileDataSource(tempFile);
		                mimebodypart.setDataHandler(new DataHandler(filedatasource));
		               
		            }
		            catch(Exception exception)
		            {
		                System.out.println("\tError in sending file not been able to attach ......\t" + exception.getMessage());
		            }
		            mimebodypart.setFileName(filename);
		            mimemultipart.addBodyPart(mimebodypart);
		            mimemessage.setContent(mimemultipart);
		            transport = sendMailSession.getTransport("smtp");
		            Transport.send(mimemessage);
		            tempFile.deleteOnExit();
		        }
		        catch(Exception messagingexception)
		        {
		            System.out.println(messagingexception.toString());
		        }
		    }
	
	 public void sendMessageMultipleToWithNameWithBCC(String mailfrom, String mailto, String mailcc[], String mailbcc, String subject, String message, String fromName)	throws REVMailException{
			
			String recipientto = mailto;
			String bcc = mailbcc;
			
			Date lgmt_date = new Date(System.currentTimeMillis());
			try{
				String[] recipientList = recipientto.split(",");
				String[] bccList = bcc.split(",");
				InternetAddress[] recipientAddress = new InternetAddress[recipientList.length];
				InternetAddress[] bccAddress = new InternetAddress[bccList.length];
				
				int counter = 0;
				for (String recipient : recipientList){
					recipientAddress[counter] = new InternetAddress(recipient.trim());
					counter++;
				}
				
				int counter1 = 0;
				for (String recipient : bccList){
					bccAddress[counter1] = new InternetAddress(recipient.trim());
					counter1++;
				}
				
				
				Message newMessage = new MimeMessage(sendMailSession);
				newMessage.setFrom(new InternetAddress(mailfrom, fromName));
				
				newMessage.setRecipients(javax.mail.Message.RecipientType.TO, recipientAddress);
				
				InternetAddress ia[] = new InternetAddress[mailcc.length];
				for(int x = 0; x < mailcc.length; x++){
					ia[x] = new InternetAddress(mailcc[x]);
				}

				newMessage.setRecipients(javax.mail.Message.RecipientType.CC, ia);
				newMessage.setRecipients(javax.mail.Message.RecipientType.BCC, bccAddress);
				newMessage.setSubject(subject);
				newMessage.setSentDate(lgmt_date);
				String EmailContent = "<html><head><title></title></head><body bgcolor=\"#FFFFFF\">";
				EmailContent = EmailContent + message;
				EmailContent = EmailContent + "</body></html>";
				newMessage.setContent(EmailContent, "text/html");
				transport = sendMailSession.getTransport("smtp");
				Transport.send(newMessage);
			}
			catch(Exception m){
				System.out.println(m.toString());
			}
		}
	 

    private boolean submitCommand(String command)
        throws REVMailException
    {
        try
        {
            output.print(command + "\r\n");
            serverReply = input.readLine();
            return serverReply.charAt(0) == '4' || serverReply.charAt(0) == '5';
        }
        catch(Exception e)
        {
            throw new REVMailException(e.getMessage());
        }
    }

    public String getServerReply()
    {
        return serverReply;
    }

    public int getPort()
    {
        return port;
    }

    public void setPort(int newPort)
    {
        port = newPort;
    }

    public String getSmtpServer()
    {
        return smtpServer;
    }

    public void setSmtpServer(String newSmtpServer)
    {
        smtpServer = newSmtpServer;
    }

    public void logout()
        throws REVMailException
    {
        try
        {
            if(submitCommand("Quit"))
            {
                throw new REVMailException("Error during QUIT command");
            }
            input.close();
            output.flush();
            output.close();
            smtp.close();
        }
        catch(Exception exception) { }
    }
}
