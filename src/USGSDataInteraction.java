import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.Scanner;

import static java.lang.System.exit;

public class USGSDataInteraction {

    public static void main (String []args) {

        USGSDataModel model = USGSDataModel.loadUSGSModel();
        USGSDataView view = new USGSDataView();
        USGSDataController controller = new USGSDataController(model, view);

        controller.inputLoop();

    }

}


class USGSDataModel{
	
		private String connectionUrl;
		private USGSDatabase db;
		private Connection conn;

    public USGSDataModel(){
        this.db = new USGSDatabase();
        this.connectionUrl = "jdbc:sqlserver://localhost:1433;databaseName=master;user=sa;password=yourStrong(!)Password";
        this.conn = db.getConnection(connectionUrl);
    }

    public static USGSDataModel loadUSGSModel(){
        return new USGSDataModel();
    }

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
          System.out.println("1. Count any column\n");
          System.out.println("==================\n");
          System.out.println("2. Search data?\n");
          System.out.println("==================\n");
          System.out.println("3. Delete data?\n");
          System.out.println("===================\n");
          System.out.println("4. Quit\n");
          System.out.println("==================\n");

          System.out.println(menu);
    }

    public void displayCount(){

    }

    public void displaySearch(){

    }

    public void displayDelete(){

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
			//COUNT
            case 1:
				view.displayCount();
                System.out.println("FACE");
                int [] countArray = countLoop();

				break;
            //SEARCH
			case 2:
				view.displaySearch();
                searchLoop();

				break;
            //DELETE
			case 3:
                view.displayDelete();
                delLoop();


				
				break;
            case 4:
                System.out.println("Exiting program, good bye!");
                exit(0);
            default:
                System.out.println("Invalid Choice");
			}
			System.out.print("Do you want to perform another action?[y/n] ");
        }while(input.next().equals("y"));

	}


	private int[] countLoop(){
        Scanner input = new Scanner (System.in);
        int [] tracker = new int[]{4,4};
        boolean valueOrRange = false;
        boolean numColumns = false;
        int val = 4;
        int col = 4;
        System.out.println("DOOKIE");
        while(!valueOrRange && !numColumns){

            if(!valueOrRange) {
                try {
                    System.out.println("Would you like to search on Value(1), Range(2) or Neither(0) ?");
                    val = input.nextInt();
                } catch (InputMismatchException e) {
                    input.next();
                    System.out.println("Please Input either 1 2 or 0");
                    continue;
                }

                if(val == 1 || val == 2 || val == 0){
                    valueOrRange = true;
                    tracker[0] = val;
                }
            }



            if(!numColumns){
                try {
                    System.out.println("Would you like to search on a Single Column(1), Several Columns(2) or Neither(0) ?");
                    col = input.nextInt();
                } catch (InputMismatchException e) {
                    System.out.print("Please Input either 1 2 or 0");
                    continue;
                }
                if(col == 1 || col == 2 || col == 0){
                    numColumns = true;
                    tracker[1] = col;
                }
            }
            
        }
        return tracker;

    }

    private void searchLoop(){

    }

    private void delLoop(){

    }



}




