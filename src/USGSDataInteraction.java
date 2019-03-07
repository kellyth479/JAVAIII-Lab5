import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
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

        System.out.println("==================");
        System.out.println("       Menu       ");
        System.out.println("==================");
        System.out.println("1. Count any column");
        System.out.println("==================");
        System.out.println("2. Search data?");
        System.out.println("==================");
        System.out.println("3. Delete data?");
        System.out.println("===================");
        System.out.println("4. Quit");
        System.out.println("==================");

//      StringBuilder menu = new StringBuilder();
//      menu.append("==================");
//      System.out.println(menu);
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
    HashMap<String, Double> value = new HashMap<String, Double>();
    HashMap<String, Double[]> range = new HashMap<String, Double[]>();
    HashMap<String, Double[]> column = new HashMap<String, Double[]>();

	public USGSDataController(USGSDataModel model, USGSDataView view) {
		this.model = model;
		this.view = view;
		
	}
	
	public void inputLoop(){
		Scanner input = new Scanner (System.in);
		do {
			view.displayMenu();
			//TODO Handle faulty input
			int choice = input.nextInt();
			input.nextLine();

			
			switch(choice) {
			//COUNT
            case 1:
                solicitValues("LATITUDE");
                solicitValues("LONGITUDE");
                solicitValues("DEPTH");
                solicitValues("MAG");
                buildCountQuery();
				break;
            //SEARCH
			case 2:
                solicitValues("LATITUDE");
                solicitValues("LONGITUDE");
                solicitValues("DEPTH");
                solicitValues("MAG");
                buildSearchQuery();
				break;
            //DELETE
			case 3:
                solicitValues("LATITUDE");
                solicitValues("LONGITUDE");
                solicitValues("DEPTH");
                solicitValues("MAG");
                buildDeleteQuery();
				break;
            case 4:
                System.out.println("Exiting program, good bye!");
                exit(0);
            default:
                System.out.println("Invalid Choice");
			}
			range.clear();
			value.clear();
			column.clear();
			System.out.print("Do you want to perform another action?[y/n] ");
        }while(input.next().equals("y"));
	}


	private double[] countLoop(){
        Scanner input = new Scanner (System.in);
        double [] tracker = new double[]{4,4,4,4};
        boolean valueOrRange = false;
        boolean numColumns = false;
        double val = 4;
        double searchValue;
        double col = 4;
        System.out.println("DOOKIE");
        while(!valueOrRange && !numColumns){

            if(!valueOrRange) {
                try {
                    System.out.println("Would you like to search on Value(1), Range(2) or Neither(0) ?");
                    val = input.nextDouble();
                } catch (InputMismatchException e) {
                    input.next();
                    System.out.println("Please Input either 1 2 or 0");
                    continue;
                }

                if(val == 1 ){
                    //get the value -- 1000 < value > -1000
                    try {
                        System.out.println("Please enter a value between -1000 and 1000");
                        searchValue = input.nextDouble();
                    } catch (InputMismatchException e) {
                        input.next();
                        System.out.println("Please Input either 1 2 or 0");
                        continue;
                    }
                }else if(val == 2){
                    //get
                }else if(val == 3){

                }else{
                    System.out.println("Please Input either 1 2 or 0");
                }
                valueOrRange = true;
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

    private int[] searchLoop(){
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

    private int[] delLoop(){
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


    public void solicitValues(String columnName){
        Scanner input = new Scanner (System.in);
        int yesFlag = -1;
        int rangeOrValue = -1;
        boolean complete = false;

        System.out.println("###############################################################################");
        System.out.println("Beginning query building process:");
        System.out.println("###############################################################################");
        System.out.println("\n");

        //Longitude Interaction:
        while(!complete) {

            try {
                System.out.println("Do you want to search on "+columnName+"? Enter 0 for Yes Enter 1 for No");
                yesFlag = input.nextInt();
            } catch (InputMismatchException e) {
                input.next();
                System.out.println("Type Mismatch - Please Input Either 1 or 2 or 0");
                continue;
            }
            //User must enter 1 or 2 or 0
            if(yesFlag > 2 || yesFlag < 1){
                System.out.println("You Must Input Either 1 or 2 or 0");
                continue;
            }
            //If user wants to search on Longitude...
            if(yesFlag == 1){
                try {
                    System.out.println("Do you want to search on a specific value(1) or a range of values(2) or neither (0)?(Enter 1 or 2 or 0)");
                    rangeOrValue = input.nextInt();
                } catch (InputMismatchException e) {
                    input.next();
                    System.out.println("Please Input Either 1 or 2 or 0");
                    continue;
                }
                //If user wants to search on a specific value
                if(rangeOrValue == 1){
                    double longInput = -1;
                    try {
                        System.out.println("Please enter a value between -200 and 200");
                        longInput = input.nextDouble();
                    } catch (InputMismatchException e) {
                        input.next();
                        System.out.println("You may only input a value between -200 and 200");
                        continue;
                    }
                    if(-200 > longInput || longInput > 200){
                        System.out.println("You may only input a value between -200 and 200");
                        continue;
                    }
                    //Add user's value to the values hashmap
                    value.put(columnName, longInput);
                    complete = true;

                    //If user wants to search between a range of values
                    }else if(rangeOrValue == 2 ){
                    double rangeInput1 = -1;
                    double rangeInput2 = -1;
                        //Get first value
                        try {
                            System.out.println("Please enter a value between -200 and 200");
                            rangeInput1 = input.nextDouble();
                        } catch (InputMismatchException e) {
                            input.next();
                            System.out.println("You may only input a value between -200 and 200");
                            continue;
                        }
                        if(-200 > rangeInput1 || rangeInput1 > 200){
                            System.out.println("You may only input a value between -200 and 200");
                            continue;
                        }
                        //Get second value
                        try {
                            System.out.println("Please enter a value between -200 and 200");
                            rangeInput2 = input.nextDouble();
                        } catch (InputMismatchException e) {
                            input.next();
                            System.out.println("You may only input a value between -200 and 200");
                            continue;
                        }
                        if(-200 > rangeInput2 || rangeInput2 > 200){
                            System.out.println("You may only input a value between -200 and 200");
                            continue;
                        }
                        Double[] longArray = new Double[]{rangeInput1,rangeInput2};
                        range.put(columnName, longArray);
                        complete = true;

                    }else if(rangeOrValue == 0) {

                    }else{
                        System.out.println("Please Input Either 1 or 2 or 0");
                        continue;
                }

            }
            System.out.println("VALUE HASHMAP:");
            value.forEach((key, value) -> System.out.println(key + ":" + value));
            System.out.println("RANGE HASHMAP:");
            range.forEach((key, value) -> System.out.println(key + ":" + value));
            System.out.println("COLUMN HASHMAP:");
            column.forEach((key, value) -> System.out.println(key + ":" + value));
        }



    }



    public StringBuilder buildCountQuery(){

	    StringBuilder query = new StringBuilder();
        //Build Operation line
	    if(column.size() != 0){
            System.out.println("###############################################################################");
	        System.out.println("Returning distinct results, only possible when using count on multiple columns:");
	        System.out.println("###############################################################################");
	        query.append("SELECT COUNT (DISTINCT ");
	    }else{
	        query.append("SELECT COUNT (*)\n");
	    }
        //Append column names to search
//        (Map.Entry<String, Object> entry : map.entrySet()) {
//            String key = entry.getKey();
//            Object value = entry.getValue();
//        }

        //Append Where Clause
//        (Map.Entry<String, Object> entry : map.entrySet()) {
//            String key = entry.getKey();
//            Object value = entry.getValue();
//        }

	    return query;
    }

    public StringBuilder buildSearchQuery(){

        StringBuilder query = new StringBuilder();

        return query;
    }

    public StringBuilder buildDeleteQuery(){

        StringBuilder query = new StringBuilder();

        return query;
    }


}




