package demo;

import java.sql.*;

public class BidService {

    public static String getLatestBid() {
        String bidPrice = "0";
        try {
            //Connection con = DriverManager.getConnection("jdbc:db2://10.1.14.86:50000/DBMSTC", "db2inst1", "db2inst1");
        	
        	Connection con;
        	Class.forName("com.ibm.db2.jcc.DB2Driver");
        	con = DriverManager.getConnection("jdbc:db2://10.1.14.86:50000/DBMSTC", "db2inst1", "db2inst1");
        			
            PreparedStatement ps = con.prepareStatement(
                " SELECT * FROM EPROC.EP_PRICE_BID_DATA WHERE EVENT_ID = 20467 AND HEADER_ID = '2' AND VENDOR_REF_ID = 237");

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                bidPrice = rs.getString("BID_AMOUNT");
            }

            con.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return bidPrice;
    }
}
