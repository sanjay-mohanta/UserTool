<%response.addHeader("Access-Control-Allow-Origin","*");%>
<%@ page import = "java.sql.*" %>
<%@ page import = "java.text.*" %>
<%@ page import = "java.util.*" %>
<%@ page import = "javax.sql.*" %>
<%@ page import = "javax.naming.*" %>

<%


java.sql.Connection con=null;
javax.sql.DataSource ds = null;
try {
Class.forName("com.ibm.db2.jcc.DB2Driver");
con = DriverManager.getConnection("jdbc:db2://10.1.14.86:50000/DBMSTC", "db2inst1","db2inst1");

//out.println("Looking up datasource :"+ds);
if(con==null){
try { 	
		Statement stmt=con.createStatement();
		stmt.execute("set schema scrap");
		stmt.close();
		System.out.println("Connection successful...");
	} catch (SQLException sqle) {
	//out.println("Failed to connect with database"+sqle);
	System.out.println("Scrap mstc Failed to connect with database");
	}
}
//out.println("Connection initialized :"+con);
//out.println("Status of Connection ---> IsClose :"+con.isClosed());
}
catch (Exception e) {
System.out.println("Scrap mstc Connection cannot be initialized:");
e.printStackTrace();
}
%> 