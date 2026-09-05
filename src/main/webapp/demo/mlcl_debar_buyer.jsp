<%@ page import="java.sql.*" %> 
<%!
	 

    /* ----------------------------------------------------
       Method 1: Check buyer debar 
       ---------------------------------------------------- */
public boolean isBuyerDebarredMethod(
        Connection con,
        String buyerRefId,
        String aucRefId
) throws SQLException {

    boolean isBuyerDebarred = false;

    String sellerRefId = getSellerRefIdByAuction(con, aucRefId);

    String sql =
        "SELECT 1 FROM SCRAP.SELLER_DEBAR_MLCL " +
        "WHERE BUYER_REF_ID = ? " +
        "AND SELLER_REF_ID = ? " +
        "AND STATUS = 'y' " +
        "AND (UNBAR_DATE > CURRENT TIMESTAMP OR UNBAR_DATE IS NULL) " +
        "FETCH FIRST 1 ROW ONLY";

    try (PreparedStatement ps = con.prepareStatement(sql)) {
        ps.setString(1, buyerRefId);
        ps.setString(2, sellerRefId);

        try (ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {        
                return true;        
            }
        }
    }

    sql =
        "SELECT 1 FROM SCRAP.BUYER_MASTER BM, SCRAP.SELLER_DEBAR_MLCL SDM " +
        "WHERE SDM.PAN_NO = BM.PAN_NO " +
        "AND SDM.PAN_NO IS NOT NULL " +
        "AND BM.BUYER_REF_ID = ? " +
        "AND SDM.SELLER_REF_ID = ? " +
        "AND STATUS = 'y' " +
        "AND (UNBAR_DATE > CURRENT TIMESTAMP OR UNBAR_DATE IS NULL) " +
        "FETCH FIRST 1 ROW ONLY";

    try (PreparedStatement ps = con.prepareStatement(sql)) {
        ps.setString(1, buyerRefId);
        ps.setString(2, sellerRefId);

        try (ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {        
                return true;
            }
        }
    }

    return false;   // Only false if no record found in both queries
}
	
	
	public String getSellerRefIdByAuction(
        Connection con,
        String aucRefId
	) throws SQLException {

		String sellerRefId = "0";

		String sql =
			"SELECT SELLER_REF_ID " +
			"FROM scrap.AUCTION_MASTER " +
			"WHERE AUC_REF_ID = ?";

		try (PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setString(1, aucRefId);

			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					sellerRefId = rs.getString(1);
				}
			}
		}

		return sellerRefId; // 0 means not found
	}

%>
 
