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

        System.out.println("###################");
        System.out.println("Select an Operation");
        System.out.println("###################");
        System.out.println("1. Count any column");
        System.out.println("###################");
        System.out.println("2. Search data?");
        System.out.println("###################");
        System.out.println("3. Delete data?");
        System.out.println("###################");
        System.out.println("4. Quit");
        System.out.println("###################");
    }

    public void beginningQuery(){
        System.out.println("###############################################################################");
        System.out.println("Beginning query building process:");
        System.out.println("###############################################################################");
        System.out.println("\n");
    }

    public void countQueryOutput(){
        System.out.println("###############################################################################");
        System.out.println("You are using the COUNT function, you may only select 1 column to count");
        System.out.println("###############################################################################");
    }
}

class USGSDataController {
	USGSDataModel model;
	USGSDataView view;
    HashMap<String, Double> value = new HashMap<String, Double>();
    HashMap<String, Double[]> range = new HashMap<String, Double[]>();
    HashMap<String, Double> column = new HashMap<String, Double>();

//    private class USGSHashMaps{
//        private HashMap<String, Double> value = new HashMap<String, Double>();
//        private HashMap<String, Double[]> range = new HashMap<String, Double[]>();
//        private HashMap<String, Double> column = new HashMap<String, Double>();
//    }

	public USGSDataController(USGSDataModel model, USGSDataView view) {
		this.model = model;
		this.view = view;
	}

	public void inputLoop(){
		Scanner input = new Scanner (System.in);
//        USGSHashMaps queryHash = new USGSHashMaps();


		do {
			view.displayMenu();
			//TODO Handle faulty input
			int choice = input.nextInt();
			input.nextLine();


			switch(choice) {
			//COUNT
            case 1:
                view.beginningQuery();
                solicitColumns(1);
                solicitValues("LATITUDE");
                solicitValues("LONGITUDE");
                solicitValues("DEPTH");
                solicitValues("MAG");
                StringBuilder countQuery = buildCountQuery();
                String execCountQuery = countQuery.toString();
                model.showData(execCountQuery);
				break;
            //SEARCH
			case 2:
                view.beginningQuery();
                solicitColumns(0);
                solicitValues("LATITUDE");
                solicitValues("LONGITUDE");
                solicitValues("DEPTH");
                solicitValues("MAG");
                StringBuilder searchQuery = buildSearchQuery();
                String execSearchQuery = searchQuery.toString();
                model.showData(execSearchQuery);
				break;
            //DELETE
			case 3:
                view.beginningQuery();
                solicitColumns( 0);
                solicitValues("LATITUDE");
                solicitValues("LONGITUDE");
                solicitValues("DEPTH");
                solicitValues("MAG");
                StringBuilder deleteQuery = buildDeleteQuery();
                String execDeleteQuery = deleteQuery.toString();
                model.showData(execDeleteQuery);
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
//            queryHash.range.clear();
//            queryHash.value.clear();
//            queryHash.column.clear();
			System.out.println("Do you want to perform another action?");
            System.out.println("'y' to continue, any other character to quit");
        }while(input.next().equals("y"));
	}

    //asks the user which columns they want to search on.
    //The "int count" argument indicates if we are building a query for the Count operation, which will only accept 1 column
    //when "int count" == 1 we only allow the user to select 1 column to query on
    public void solicitColumns(int count){
        Scanner input = new Scanner (System.in);
        int yesFlag = -1;
        double columndub = -201;
        String [] columns = new String[]{"LATITUDE","LONGITUDE","DEPTH","MAG"};

        if(count == 1){
            //Inform user they can only select one column to perform the count operation on
            view.countQueryOutput();
        }

        //Columns to select

            for(int i = 0;i<columns.length;i++){
                try {
                    System.out.println("Do you want to perform this operation on the column "+columns[i]+"?");
                    System.out.println("Enter 1 for Yes");
                    System.out.println("Enter 0 for No");
                    System.out.println(i);
                    yesFlag = input.nextInt();
                } catch (InputMismatchException e) {
                    input.next();
                    System.out.println("Type Mismatch - Please Input Either 1 or 0");
                    continue;
                }
                //User must enter 1 or 2 or 0
                if(yesFlag > 1 || yesFlag < 0){
                    System.out.println("You Must Input Either 1 or 0");
                    i = i-1;
                    System.out.println(i);
                    continue;
                }
                //If user wants to search on columnName...
                if(yesFlag == 1){
                    column.put(columns[i], columndub);
//                    System.out.println("COLUMN HASHMAP:");
//                    column.forEach((key, value) -> System.out.println(key + ":" + value));
                    //only one column may be selected to count on
                    if(count == 1){
                        break;
                    }
            }
        }
    }

    //asks the user if they want to qualify the search by limiting the values of columns they choose
    //could be refactored to mirror the solicitColumns method using an array of the columns
    public void solicitValues(String columnName){
        Scanner input = new Scanner (System.in);
        int yesFlag = -1;
        int rangeOrValue = -1;
        boolean complete = false;

        //Columns to search on and values or ranges
        while(!complete) {

            try {
                System.out.println("Do you want to search on "+columnName+"?");
                System.out.println("Enter 1 for Yes");
                System.out.println("Enter 0 for No");
                yesFlag = input.nextInt();
            } catch (InputMismatchException e) {
                input.next();
                System.out.println("Type Mismatch - Please Input Either 1 or 0");
                continue;
            }
            //User must enter 1 or 2 or 0
            if(yesFlag > 1 || yesFlag < 0){
                System.out.println("You Must Input Either 1 or 0");
                continue;
            }
            //If user wants to search on columnName...
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
                        System.out.println("Please enter a value between -200 and 700");
                        longInput = input.nextDouble();
                    } catch (InputMismatchException e) {
                        input.next();
                        System.out.println("You may only input a value between -200 and 700");
                        continue;
                    }
                    if(-200 > longInput || longInput > 700){
                        System.out.println("You may only input a value between -200 and 700");
                        continue;
                    }
                    //Add user's value to the values hashmap
//                    queryHash.value.put(columnName, longInput);
                    value.put(columnName, longInput);

                    //If user wants to search between a range of values
                    }else if(rangeOrValue == 2 ){
                    double rangeInput1 = -1;
                    double rangeInput2 = -1;
                        //Get first value
                        try {
                            System.out.println("Please enter a value between -200 and 700");
                            rangeInput1 = input.nextDouble();
                        } catch (InputMismatchException e) {
                            input.next();
                            System.out.println("You may only input a value between -200 and 700");
                            continue;
                        }
                        if(-200 > rangeInput1 || rangeInput1 > 700){
                            System.out.println("You may only input a value between -200 and 700");
                            continue;
                        }
                        //Get second value
                        try {
                            System.out.println("Please enter a value between -200 and 700");
                            rangeInput2 = input.nextDouble();
                        } catch (InputMismatchException e) {
                            input.next();
                            System.out.println("You may only input a value between -200 and 700");
                            continue;
                        }
                        if(-200 > rangeInput2 || rangeInput2 > 700){
                            System.out.println("You may only input a value between -200 and 700");
                            continue;
                        }
                        Double[] longArray = new Double[]{rangeInput1,rangeInput2};
//                        queryHash.range.put(columnName, longArray);
                        range.put(columnName, longArray);

                    }else if(rangeOrValue == 0) {
//                        queryHash.column.put(columnName, columndub);
//                        column.put(columnName, columndub);

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
            complete = true;
        }



    }

    public StringBuilder buildCountQuery(){

	    StringBuilder query = new StringBuilder();

        //Build Operation line
	    if(column.size() != 0){
	        query.append("SELECT COUNT (EQ.");
            for (String key : column.keySet()) {
                query.append(key);
//                System.out.println("Key: " + key + ", Value: " + column.get(key));
            }
            query.append(")");
	        query = buildGeneralQuery(query);

	    }else{
	        query.append("SELECT COUNT (*) FROM EARTHQUAKE EQ");
	    }
        query.append(";");
        System.out.println(query);
	    return query;
    }

    public StringBuilder buildSearchQuery(){

        StringBuilder query = new StringBuilder();

        //Build Operation line
        if(column.size() != 0){
            query.append("SELECT EQ.");
            for (String key : column.keySet()) {
                query.append(key);
                query.append(", EQ.");
//                System.out.println("Key: " + key + ", Value: " + column.get(key));
            }
            query = removeWhiteSpace(query, 5);
            query = buildGeneralQuery(query);

        }else{
            query.append("SELECT * ");
            query = buildGeneralQuery(query);
        }
        query.append(";");
        System.out.println(query);
        return query;
    }

    public StringBuilder buildDeleteQuery(){

        StringBuilder query = new StringBuilder();

        //Build Operation line
        if(column.size() != 0){
            query.append("DELETE EQ.");
            for (String key : column.keySet()) {
                query.append(key);
                query.append(", EQ.");
//                System.out.println("Key: " + key + ", Value: " + column.get(key));
            }
            query = removeWhiteSpace(query, 5);
            query = buildGeneralQuery(query);

        }else{
            query.append("DELETE * ");
            query = buildGeneralQuery(query);
        }
        query.append(";");
        System.out.println(query);
        return query;
    }

    public StringBuilder buildGeneralQuery(StringBuilder query){
//            System.out.println("###################");
//            System.out.println(query);
//            System.out.println("###################");

//            System.out.println("###################");
//            System.out.println(query);
        if(value.size() != 0 || range.size() != 0){
            query.append(" FROM EARTHQUAKE_DATA EQ WHERE EQ.");
        }else{
            query.append(" FROM EARTHQUAKE_DATA EQ");
        }

        if(value.size() != 0){
//            query.append(" FROM EARTHQUAKE_DATA EQ WHERE EQ.");
            for (String key : value.keySet()) {
                query.append(key);
                query.append(" = ");
                query.append(value.get(key));
                query.append(" AND EQ.");
//                System.out.println("Key: " + key + ", Value: " + value.get(key));
            }
            query = removeWhiteSpace(query, 8);
        }

        if(range.size() != 0){
            for (String key : range.keySet()) {
                Double[] dubArray = new Double[2];
                query.append(key);
                query.append(" BETWEEN ");
                dubArray = range.get(key);
                query.append(dubArray[0].toString());
                query.append(" AND ");
                query.append(dubArray[1].toString());
//                query.append(range.get(key));
                query.append(" AND EQ.");
//                System.out.println("Key: " + key + ", Value: " + value.get(key));
            }
            query = removeWhiteSpace(query, 8);
        }
	    return query;
    }


    public StringBuilder removeWhiteSpace(StringBuilder str, int count){

	    for(int i = 0;i<count;i++){
            str.deleteCharAt(str.length()-1);
        }
        return str;

    }


}




