import java.io.PrintStream;
import java.sql.*;

public class ConnectionMaker
{

    public ConnectionMaker()
    {
        try
        {
            Class.forName("com.ibm.db2.jcc.DB2Driver");
        }
        catch(Exception exx)
        {
            System.out.println("Cannot make connection ..." + exx);
        }
    }

    public Connection getConnection()
    {
        Connection newCon = null;
        try
        {
            newCon = DriverManager.getConnection("jdbc:db2://10.1.14.86:50000/DBMSTC", "db2inst1", "db2inst1");
            if(newCon == null)
            {
                throw new Exception("Connection is stale or null ...");
            }
            if(!newCon.isClosed())
            {
                Statement stmt = newCon.createStatement();
                stmt.execute("set schema db2inst1");
                stmt.close();
                System.out.println("Connected successfully to coal schema");
            }
        }
        catch(Exception ecc)
        {
            System.out.println("Error Cannot set schema connection ..." + ecc);
            newCon = null;
        }
        return newCon;
    }

    public Connection getConnection(String schema)
    {
        Connection newCon = null;
        try
        {
            newCon = DriverManager.getConnection("jdbc:db2://10.1.14.86:50000/DBMSTC", "db2inst1", "db2inst1");
            if(newCon == null)
            {
                throw new Exception("Connection is stale or null ...");
            }
            if(!newCon.isClosed())
            {
                Statement stmt = newCon.createStatement();
                stmt.execute("set schema " + schema);
                stmt.close();
                System.out.println("Connected successfully to " + schema + " schema");
            }else
			{
				   System.out.println("Connection closed");
			}
        }
        catch(Exception ecc)
        {
            System.out.println("Error Cannot set schema connection ..." + ecc);
            newCon = null;
        }
        return newCon;
    }
}
