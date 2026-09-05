package mstc;
import java.io.InputStream;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.cert.CertificateFactory;
import java.security.cert.X509CRL;
import java.security.cert.X509CRLEntry;
import java.security.cert.X509Certificate;

public class DSCRevocationChecker {

    //private static final String CRL_URL = "http://www.ncodesolutions.com/repository/ncodeca22subca1.crl";
	
	private String getCommonName(X509Certificate certificate) {

	    String subject = certificate.getSubjectX500Principal().getName();

	    for (String part : subject.split(",")) {

	        part = part.trim();

	        if (part.startsWith("CN=")) {
	            return part.substring(3);
	        }
	    }

	    return "";
	}

    public RevocationResult checkRevocation(String serialNumber, String CRL_URL)
            throws Exception {

        if (serialNumber == null || serialNumber.trim().isEmpty()) {

            return new RevocationResult( "ERROR", "Certificate serial number is empty." );
        }

        String cleanSerial =  serialNumber.trim().replace(":", "").replace(" ", "").replace("-", "");

        BigInteger certificateSerial;

        try {

            certificateSerial = new BigInteger(cleanSerial, 16);

        } catch (NumberFormatException e) {

            return new RevocationResult("ERROR", "Invalid certificate serial number. " + "Enter the serial number in hexadecimal format." );
        }


        HttpURLConnection connection = null;

        try {

            URL url = new URL(CRL_URL);

            connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("GET");

            connection.setConnectTimeout(15000);
            connection.setReadTimeout(30000);

            connection.setUseCaches(false);


            int responseCode =
                connection.getResponseCode();

            if (responseCode != HttpURLConnection.HTTP_OK) {

                throw new Exception("Unable to download CRL. HTTP response: " + responseCode);
            }


            CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");


            try (InputStream inputStream = connection.getInputStream()) {

                X509CRL crl = (X509CRL) certificateFactory.generateCRL(inputStream);

                X509CRLEntry revokedEntry = crl.getRevokedCertificate(certificateSerial);


                if (revokedEntry != null) {

                    StringBuilder msg =
                        new StringBuilder();

                    msg.append("Certificate serial number ")
                       .append(cleanSerial)
                       .append(" is present in the CRL.");

                    msg.append("<br>");

                    msg.append("Revocation Date: ")
                       .append(revokedEntry.getRevocationDate());

                    msg.append("<br>");

                    msg.append("CRL Issuer: ")
                       .append(crl.getIssuerX500Principal());

                    return new RevocationResult(
                        "REVOKED",
                        msg.toString()
                    );

                } else {

                    StringBuilder msg =
                        new StringBuilder();

                    msg.append("Certificate serial number ")
                       .append(cleanSerial)
                       .append(" was not found in the CRL.");

                    msg.append("<br>");

                    msg.append("CRL Issuer: ")
                       .append(crl.getIssuerX500Principal());

                    msg.append("<br>");

                    msg.append("CRL This Update: ")
                       .append(crl.getThisUpdate());

                    msg.append("<br>");

                    msg.append("CRL Next Update: ")
                       .append(crl.getNextUpdate());

                    return new RevocationResult(
                        "NOT_REVOKED",
                        msg.toString()
                    );
                }
            }

        } finally {

            if (connection != null) {
                connection.disconnect();
            }
        }
    }
}
