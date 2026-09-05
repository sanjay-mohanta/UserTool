import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class TestItera {
	public static void main(String[] args) {
		String generated_block_code = "test1";
		HashMap<String, String> uploadDocsList = new HashMap<String, String>();
		uploadDocsList.put("BLANK_TENDER_DOCUMENT_" + generated_block_code, "blank_tender_document");
		uploadDocsList.put("GEOLOGICAL_REPORT_" + generated_block_code, "geological_report");
		uploadDocsList.put("MINE_BLOCK_SUMMARY_" + generated_block_code, "mine_block_summary");
		uploadDocsList.put("BLOCK_SPECIFIC_TENDER_DOC_" + generated_block_code, "block_specific_tender_doc");
		uploadDocsList.put("OTHER_FILE_" + generated_block_code, "other_file");

		Iterator<Map.Entry<String, String>> fileiterator = uploadDocsList.entrySet().iterator();

		// Use a while loop and the iterator methods
		while (fileiterator.hasNext()) {
			// Get the next entry (key-value pair)
			Map.Entry<String, String> entry = fileiterator.next();

			String filename = entry.getKey();
			String filecontent = entry.getValue();
			System.out.println("Key: " + filename + ", Value: " + filecontent);
		}
	}
}
