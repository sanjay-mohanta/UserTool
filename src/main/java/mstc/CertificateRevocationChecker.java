package mstc;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.cert.X509CRL;
import java.security.cert.X509CRLEntry;
import java.util.Collection;

import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.x509.CRLDistPoint;
import org.bouncycastle.asn1.x509.DistributionPoint;
import org.bouncycastle.asn1.x509.GeneralName;
import org.bouncycastle.asn1.x509.GeneralNames;

public class CertificateRevocationChecker {

	public static Result check(String pemCertificate) throws Exception {

		// Convert PEM to X509Certificate
		X509Certificate certificate = convertPemToCertificate(pemCertificate);

		Result result = new Result();
		
		// Contact person name from CN
		result.subjectName = getCommonName(certificate);

		// Certificate serial number
		result.serialNumber = certificate.getSerialNumber().toString(16).toUpperCase();

		System.out.println("Certificate Serial Number: " + result.serialNumber);

		// Get CRL URL from certificate
		String crlUrl = getCrlDistributionPoint(certificate);

		result.crlUrl = crlUrl;

		if (crlUrl == null) {
			throw new Exception("Certificate does not contain a CRL Distribution Point");
		}

		System.out.println("CRL URL: " + crlUrl);

		// Download CRL
		X509CRL crl = downloadCRL(crlUrl);

		// Check certificate serial number in CRL
		X509CRLEntry revokedCertificate = crl.getRevokedCertificate(certificate.getSerialNumber());

		if (revokedCertificate != null) {

			result.revoked = true;

			result.revocationDate = revokedCertificate.getRevocationDate();

			System.out.println("CERTIFICATE IS REVOKED");

		} else {

			result.revoked = false;

			System.out.println("CERTIFICATE IS NOT REVOKED");
		}

		return result;
	}
	
	private static String getCommonName(
	        X509Certificate certificate) {

	    String subjectDN =
	            certificate.getSubjectX500Principal().getName();

	    String[] attributes = subjectDN.split(",");

	    for (String attribute : attributes) {

	        attribute = attribute.trim();

	        if (attribute.startsWith("CN=")) {
	            return attribute.substring(3);
	        }
	    }

	    return "";
	}

	private static X509Certificate convertPemToCertificate(String pem) throws Exception {

		String cleanPem = pem.replace("-----BEGIN CERTIFICATE-----", "").replace("-----END CERTIFICATE-----", "")
				.replaceAll("\\s+", "");

		byte[] certificateBytes = java.util.Base64.getDecoder().decode(cleanPem);

		CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");

		return (X509Certificate) certificateFactory.generateCertificate(new ByteArrayInputStream(certificateBytes));
	}

	private static String getCrlDistributionPoint(X509Certificate certificate) throws Exception {

		byte[] extensionValue = certificate.getExtensionValue("2.5.29.31");

		if (extensionValue == null) {
			return null;
		}

		ASN1Primitive primitive;

		try (ASN1InputStream asn1InputStream = new ASN1InputStream(extensionValue)) {

			primitive = asn1InputStream.readObject();
		}

		byte[] octets = ((org.bouncycastle.asn1.ASN1OctetString) primitive).getOctets();

		CRLDistPoint distPoint = CRLDistPoint.getInstance(ASN1Primitive.fromByteArray(octets));

		for (DistributionPoint distributionPoint : distPoint.getDistributionPoints()) {

			if (distributionPoint.getDistributionPoint() == null) {
				continue;
			}

			GeneralNames generalNames = GeneralNames.getInstance(distributionPoint.getDistributionPoint().getName());

			for (GeneralName generalName : generalNames.getNames()) {

				if (generalName.getTagNo() == GeneralName.uniformResourceIdentifier) {

					return generalName.getName().toString();
				}
			}
		}

		return null;
	}

	private static X509CRL downloadCRL(String crlUrl) throws Exception {

		URL url = new URL(crlUrl);

		HttpURLConnection connection = (HttpURLConnection) url.openConnection();

		connection.setConnectTimeout(10000);
		connection.setReadTimeout(10000);
		connection.setRequestMethod("GET");

		if (connection.getResponseCode() != 200) {
			throw new Exception("Unable to download CRL. HTTP status: " + connection.getResponseCode());
		}

		try (InputStream inputStream = connection.getInputStream()) {

			CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");

			return (X509CRL) certificateFactory.generateCRL(inputStream);
		}
	}
}