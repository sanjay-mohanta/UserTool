import java.io.*;
import java.net.Socket;
import java.util.Date;
import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;
import java.nio.charset.*;
public class MSTCMail
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

    public static String getStackTrace(Throwable t)
    {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw, true);
        t.printStackTrace(pw);
        pw.flush();
        sw.flush();
        return sw.toString();
    }

    public MSTCMail()
    {
        smtpServer = "";
        port = 25;
        props = new Properties();
    }

    public void sendMail(String mailfrom, String mailto, String subject, String message)
        throws MSTCMailException
    {
        connect();
        hail(mailfrom, mailto);
        sendMessage(mailfrom, mailto, subject, message);
        logout();
    }

    public void sendMailWithCC(String mailfrom, String mailto, String mailcc, String subject, String message)
        throws MSTCMailException
    {
        connect();
        hail(mailfrom, mailto);
        sendMessage(mailfrom, mailto, mailcc, subject, message);
        logout();
    }

    public void sendMailWithMultiCC(String mailfrom, String mailto, String mailcc1[], String subject, String message)
        throws MSTCMailException
    {
        connect();
        hail(mailfrom, mailto);
        sendMessage(mailfrom, mailto, mailcc1, subject, message);
        logout();
    }
	
	public void sendMailWithMultiToMultiCC(String mailfrom, String mailto, String mailcc1[], String subject, String message)
        throws MSTCMailException
    {
        connect();
        hail(mailfrom, mailto);
        sendMessageMultipleToMultipleCC(mailfrom, mailto, mailcc1, subject, message);
        logout();
    }
	
    public void connect()
        throws MSTCMailException
    {
        try
        {
            sendMailSession = Session.getInstance(props, null);
            props.put("mail.smtp.host", smtpServer);
            props.put("mail.mime.allowutf8","true");
        }
        catch(Exception e)
        {
            throw new MSTCMailException(e.getMessage());
        }
    }

    public void hail(String s, String s1)
        throws MSTCMailException
    {
    }

    public void sendMessage(String mailfrom, String mailto, String subject, String message)
        throws MSTCMailException
    {
        Date lgmt_date = new Date(System.currentTimeMillis());
        try
        {
            Message newMessage = new MimeMessage(sendMailSession);
            newMessage.setFrom(new InternetAddress(mailfrom));
            newMessage.setRecipient(javax.mail.Message.RecipientType.TO, new InternetAddress(mailto));
            byte[] bytes = subject.getBytes(StandardCharsets.UTF_8);
            String subject_utf8 = new String(bytes, StandardCharsets.UTF_8);    
            newMessage.setSubject(subject_utf8);
            newMessage.setSentDate(lgmt_date);
            String EmailContent = "<html><head><title>MSTC</title></head><body bgcolor=\"#FFFFFF\">";
            EmailContent = EmailContent + message;
            EmailContent = EmailContent + "</body></html>";
            newMessage.setContent(EmailContent, "text/html; charset=utf-8");
            transport = sendMailSession.getTransport("smtp");
            Transport.send(newMessage);
        }
        catch(MessagingException m)
        {
            System.out.println("Invalid email address MSTCMail.java --> " + mailto);
        }
    }

    public void sendMessage(String mailfrom, String mailto, String mailcc, String subject, String message)
        throws MSTCMailException
    {
        Date lgmt_date = new Date(System.currentTimeMillis());
        try
        {
            Message newMessage = new MimeMessage(sendMailSession);
            newMessage.setFrom(new InternetAddress(mailfrom));
            newMessage.setRecipient(javax.mail.Message.RecipientType.TO, new InternetAddress(mailto));
            newMessage.setRecipient(javax.mail.Message.RecipientType.CC, new InternetAddress(mailcc));
            byte[] bytes = subject.getBytes(StandardCharsets.UTF_8);
            String subject_utf8 = new String(bytes, StandardCharsets.UTF_8);
            newMessage.setSubject(subject_utf8);
            newMessage.setSentDate(lgmt_date);
            String EmailContent = "<html><head><title>MSTC</title></head><body bgcolor=\"#FFFFFF\">";
            EmailContent = EmailContent + message;
            EmailContent = EmailContent + "</body></html>";
            newMessage.setContent(EmailContent, "text/html; charset=utf-8");
            transport = sendMailSession.getTransport("smtp");
            Transport.send(newMessage);
        }
        catch(MessagingException m)
        {
            System.out.println("Exception generated from MSTCMAil Class withcc : " + m.toString() + " defective email address :" + mailto);
        }
    }

    public void sendMessage(String mailfrom, String mailto, String mailcc[], String subject, String message)
        throws MSTCMailException
    {
        Date lgmt_date = new Date(System.currentTimeMillis());
        try
        {
            Message newMessage = new MimeMessage(sendMailSession);
            newMessage.setFrom(new InternetAddress(mailfrom));
            System.out.println("generated from MSTCMAil Class multicc :  defective email address :" + mailto + " length of mailto :" + mailto.length());
            newMessage.setRecipient(javax.mail.Message.RecipientType.TO, new InternetAddress(mailto));
            InternetAddress ia[] = new InternetAddress[mailcc.length];
            for(int x = 0; x < mailcc.length; x++)
            {
                ia[x] = new InternetAddress(mailcc[x]);
            }

            newMessage.setRecipients(javax.mail.Message.RecipientType.CC, ia);
            byte[] bytes = subject.getBytes(StandardCharsets.UTF_8);
            String subject_utf8 = new String(bytes, StandardCharsets.UTF_8);
            newMessage.setSubject(subject_utf8);           
            newMessage.setSentDate(lgmt_date);
            String EmailContent = "<html><head><title>MSTC</title></head><body bgcolor=\"#FFFFFF\">";
            EmailContent = EmailContent + message;
            EmailContent = EmailContent + "</body></html>";
            newMessage.setContent(EmailContent, "text/html; charset=utf-8");
            transport = sendMailSession.getTransport("smtp");
            Transport.send(newMessage);
        }
        catch(AddressException a)
        {
            System.out.println("Reference point :" + a.getRef());
            System.out.println("Reference position :" + a.getPos());
            System.out.println("AddressException Stack Trace Of Exception :" + getStackTrace(a));
            System.out.println("AddressException generated from MSTCMAil Class multicc : " + a.toString() + " defective email address :" + mailto + " length of mailto :" + mailto.length());
        }
        catch(MessagingException m)
        {
            System.out.println("Stack Trace Of Exception :" + getStackTrace(m));
            System.out.println("Exception generated from MSTCMAil Class multicc : " + m.toString() + " defective email address :" + mailto + " length of mailto :" + mailto.length());
        }
    }
	
	public void sendMessageMultipleToMultipleCC(String mailfrom, String mailto, String mailcc[], String subject, String message)
        throws MSTCMailException
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
            byte[] bytes = subject.getBytes(StandardCharsets.UTF_8);
            String subject_utf8 = new String(bytes, StandardCharsets.UTF_8);
            newMessage.setSubject(subject_utf8);
            newMessage.setSentDate(lgmt_date);
            String EmailContent = "<html><head><title></title></head><body bgcolor=\"#FFFFFF\">";
            EmailContent = EmailContent + message;
            EmailContent = EmailContent + "</body></html>";
            newMessage.setContent(EmailContent, "text/html; charset=utf-8");
            transport = sendMailSession.getTransport("smtp");
            Transport.send(newMessage);
        }
        catch(MessagingException m)
        {
            //System.out.println(m.toString());
        }
    }
	
    private boolean submitCommand(String command)
        throws MSTCMailException
    {
        try
        {
            output.print(command + "\r\n");
            serverReply = input.readLine();
            return serverReply.charAt(0) == '4' || serverReply.charAt(0) == '5';
        }
        catch(Exception e)
        {
            throw new MSTCMailException(e.getMessage());
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
        throws MSTCMailException
    {
        try
        {
            if(submitCommand("Quit"))
            {
                throw new MSTCMailException("Error during QUIT command");
            }
            input.close();
            output.flush();
            output.close();
            smtp.close();
        }
        catch(Exception exception) { }
    }
}
