import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

public class USGSDatabase {



	public Connection getConnection (String url) {
		Connection conn = null;

//        try {
//            Class.forName("com.mysql.jdbc.Driver");
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        }

		try {

			conn= DriverManager.getConnection(url);
			
		}catch (SQLException e){
			e.printStackTrace();
			
		}catch(Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		return conn;
		
	}
	
	public ResultSet showData(Connection conn, String sql) {
		ResultSet rs = null;
		
		try {
			java.sql.Statement stmt;
			stmt=conn.createStatement();
			System.out.println("##################");
			System.out.print(sql+" Count Returned from DB ");

			rs=stmt.executeQuery(sql);
		}catch(SQLException e) {
			e.printStackTrace();
			
		}catch(Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		return rs;
	}
	
	public int runSql (Connection conn, String sql) {
		
		try {
			System.out.println("Running Sql: " + sql);
			java.sql.Statement stmt= conn.createStatement();
			
			return stmt.executeUpdate(sql);
		}catch(SQLException e) {
			e.printStackTrace();
			
		}catch(Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		return 0;
	}
	

}
