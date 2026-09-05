import java.util.Base64;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;

public class CheckPdf {
	public static void main(String[] args) {
		
		String filecontent = "";
		
		byte[] pdfBytesfilecontent = Base64.getDecoder().decode(filecontent);
		boolean chk = isValidPdf(pdfBytesfilecontent);
		System.out.println(chk);
	}
	
	public static boolean isValidPdf(byte[] data) {
        if (data == null || data.length == 0) {
            return false;
        }

        try (PDDocument document = Loader.loadPDF(data)) {
            // Successfully loaded → valid PDF
            return document.getNumberOfPages() > 0;
        } catch (Exception e) {
            return false;
        }
    }
}

