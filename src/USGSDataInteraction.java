import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Scanner;

class USGSDataModel{
	
		String connectionUrl = "jdbc:sqlserver://localhost:1433;databaseName=master;user=sa;password=yourStrong(!)Password";
	
		USGSDatabase db = new USGSDatabase();

		Connection conn = db.getConnection(connectionUrl);
	
	public StringBuffer showData(String sql) {
		
		ResultSet rs = db.showData(conn, sql);
		StringBuffer str = new StringBuffer();
		
		try {
			ResultSetMetaData rsmd = rs.getMetaData();
			int columnsNumber = rsmd.getColumnCount();
			
			while (rs.next()) {
				for (int i =1; i<=columnsNumber; i++) {
					String columnValue= rs.getString(i);
					str.append(rsmd.getColumnName(i) + "=" + columnValue);					
				}
				str.append("");
			}
		}catch (SQLException e) {
			e.printStackTrace();
		}
		return str;
	}
	
	public void deletedata(String sql) {
		db.runSql(conn, sql);
	}
	
	public void countData(String sql) {
		db.runSql(conn, sql);
	}
	

}	

class USGSDataView {
	public USGSDataView(){
		
	}
	
public void displayMenu(){
	
	StringBuilder menu = new StringBuilder();
	
	  menu.append("==================\n");
      System.out.println("		Menu		\n");
      System.out.println("==================\n");
      System.out.println("1. Search data?\n");
      System.out.println("==================\n");
      System.out.println("2. Delete data?\n");
      System.out.println("==================\n");
      System.out.println("3. Count any column\n");
      System.out.println("===================\n");      
      System.out.println("4. Quit\n");
      System.out.println("==================\n");
      
      System.out.println(menu);
}


public void displayData() {
	
	StringBuffer showsql = new StringBuffer ("Select * from earthquake_data where ");
	String latitude, longtitude, depth, mag;
	latitude = handleRequest ("Please enter latitude: ");
	if(!latitude.equals("")) {
		showsql.append("latitude" + latitude);
		
	}
	longtitude = handleRequest("Please enter longtitude: ");
	if(!longtitude.equals("") && (!latitude.equals(""))) {
		showsql.append("");
	}
	
	
	String rlong = handleRequest("Please enter longitiude: ");
	String rlati = handleRequest("please enter latitiude: ");
	String rdepth = handleRequest("Please enter depth: ");
	String rmag	= handleRequest("Please enter mag: ");
	
	
}	


public String handleRequest (String question) {
	System.out.println(question);
	Scanner input = new Scanner (System.in);
	String userInput = input.nextLine();
	return userInput;

}
}

class USGSDataController {
	USGSDataModel model;
	USGSDataView view;
	
	public USGSDataController(USGSDataModel model, USGSDataView view) {
		this.model = model;
		this.view = view;
		
	}
	
	public void inputLoop(){
		Scanner input = new Scanner (System.in);
		do {
			view.displayMenu();
			int choice = input.nextInt();
			input.nextLine();
			
			switch(choice) {
			case 1:
				System.out.println("what do you want to search?");
				
				break;
				
			case 2:
				System.out.println("count");
				
				break;
				
			case 3:
				System.out.println("which field you want to delete?");
				
				break;
				
				default:
			}
		}while(true);


			
			
	}
}



public class USGSDataInteraction {
	
	public static void main (String []args) {
		
		
	}

}
