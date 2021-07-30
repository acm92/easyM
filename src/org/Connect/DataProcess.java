package org.Connect;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.sql.Connection; 
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Scanner;
import java.sql.ResultSet; 

import org.Measure.Measure;
import org.Measure.MeasureList;

/**
 * The DataProcess class which will extract data from the model database and create a script with given data
 * 
 * @author Ángel Ciudad Montalbán
 * @since 2021
 */
public class DataProcess {
	/** The Connection object to establish connection to the sqlite file*/
	private Connection conn;
	
	/**
	 * The default DataProcess constructor
	 */
	public DataProcess() {

		this.conn = null;

	}

	/** The DataProcess constructor with a Connection object passed as parameter*/
	public DataProcess(Connection con) {

		this.conn = con;

	}

	/**
	 * Establish connection to the metadata.sqlitedb database of the model
	 */
	public void connect() {

		try {

			//The public and static connect() ReadOpenModels method which will return the Connection object of a selected model 
			this.conn = ReadOpenModels.connect();
		//If a pbix model is not found or a SQLException is thrown
		} catch (PbixModelNotFoundException | SQLException e) {

			System.out.println(e.getMessage());
		}
	}

	/**
	 * Extracts information with SELECT SQL queries of the different tables in the connected database model.
	 * Before inserting the temp measures into the model, information about the dimensions tables need to be obtained.
	 */
	public void fetchData() {
		//An auxiliar Measure object
		Measure m;
		//Executes a SQL query
		Statement st; 
		//Stores the result of a SQL query
		ResultSet rs; 
		//To obtain input given by the user
		Scanner scan = new Scanner(System.in);
		//Strings to store the different information extracted from the database
		String reading = "", measureName = "", description = "", daxExpression = "", tableID = "", measures="", query;
		String date = "", column = "", period = "";
		//A list of options to choose from
		ArrayList<String> options = new ArrayList<String>();
		//The selection made by the user of a list of options, and an index (contents) of the contents returned by a query
		int selection, contents = 0;

		try {

			//Selecting the date dimension
			do {
 
				System.out.println("Selecciona la dimension de fechas:");
				System.out.println("----------------------------------");

				// Obtaining the dates table data
				query = "SELECT Name FROM 'Table' WHERE ModelID = 1";
				st = conn.createStatement();
				rs = st.executeQuery(query);
				//Iterates through the set returned by the statement 
				while (rs.next()) {
					contents++;
					System.out.println(contents + ". " + rs.getString(1));
					options.add(rs.getString(1));

				}

				// Choosing the table
				selection = scan.nextInt();
				//Out of range handling
				if (selection <= 0 || selection > (contents + 1))
					System.out.println("Opcion incorrecta. Elija de nuevo");

			} while (selection <= 0 || selection > (contents + 1));
			
			//Adjusts the index of the selection with the index of the list
			date = options.get((selection - 1));

			
			//Obtaining the tableID value from TableID column
			query = "SELECT ID FROM 'Table' WHERE Name = '" + date + "'";
			st = conn.createStatement();
			rs = st.executeQuery(query);
			
			//Only one value is returned
			while (rs.next())
				tableID = rs.getString(1);

			//Resets the values to zero
			selection = 0;
			contents = 0;
			options.clear();
			
			//Select the Measure table
			do {

				System.out.println("Selecciona la tabla de medidas:");
				System.out.println("----------------------------------");

				// Query execution
				query = "SELECT Name FROM 'Table' WHERE ModelID = 1";
				st = conn.createStatement();
				rs = st.executeQuery(query);
				
				//Iterating through the set obtained
				while (rs.next()) {
					contents++;
					System.out.println(contents + ". " + rs.getString(1));
					options.add(rs.getString(1));

				}

				selection = scan.nextInt();

				if (selection <= 0 || selection > (contents + 1))
					System.out.println("Opcion incorrecta. Elija de nuevo");

			} while (selection <= 0 || selection > (contents + 1));

			measures = options.get((selection - 1));

			selection = 0;
			contents = 0;
			options.clear();
			
			
			//Select the periods table
			do {

				System.out.println("Selecciona la dimension de periodos:");
				System.out.println("----------------------------------");

				query = "SELECT Name FROM 'Table' WHERE ModelID = 1";
				st = conn.createStatement();
				rs = st.executeQuery(query);

				while (rs.next()) {
					contents++;
					System.out.println(contents + ". " + rs.getString(1));
					options.add(rs.getString(1));

				}

				// Elegir la tabla
				selection = scan.nextInt();

				if (selection <= 0 || selection > (contents + 1))
					System.out.println("Opcion incorrecta. Elija de nuevo");

			} while (selection <= 0 || selection > (contents + 1));

			period = options.get((selection - 1));

			contents = 0;
			selection = 0;

			//Select the ID column of the dates dimension
			do {

				System.out.println("Selecciona la columna identificadora de la dimension de fechas:");
				System.out.println("---------------------------------------------------------------");

				//Extracts the name of said column
				query = "SELECT ExplicitName FROM 'Column' WHERE TableID = '" + tableID
						+ "' AND ExplicitName NOT LIKE 'RowNumber%'";
				st = conn.createStatement();
				rs = st.executeQuery(query);
				
				options.clear();
				while (rs.next()) {

					contents++;
					System.out.println(contents + ". " + rs.getString(1));
					options.add(rs.getString(1));
				}

				selection = scan.nextInt();

				if (selection <= 0 || selection > (contents + 1))
					System.out.println("Opcion incorrecta. Elija de nuevo");

			} while (selection <= 0 || selection > (contents + 1));

			column = options.get((selection - 1));
			selection = 0;
			contents = 0;
			options.clear();

			
			//Obtaining the measures names already stored inside the model
			do {

				System.out.println("Estas son las medidas a las que se le pueden aplicar las medidas temporales:");
				System.out.println("Por favor elije la medida a la que quieres aplicar la generación.");
				System.out.println("-------------------------------------------------------------------------------");
				
				query = "SELECT DISTINCT Name FROM Measure";
				st = conn.createStatement();
				rs = st.executeQuery(query);
						
				//Iterate through the set of measure names
				while (rs.next()) {					
					reading = rs.getString(1);
					options.add(reading);
					//Create a temporary auxiliar measure in order to detect if it is a time measure
					m = new Measure(reading);
					//If the measure found is a time measure it is ignored
					if (m.isTimeMeasure())
						continue;
					
					contents++;
					System.out.println(contents+". " + m.getName());
				}
				//If there are no measures, the program can't advance
				if(contents == 0) {
					
					System.out.println("Este modelo no tiene ninguna medida creada. "
							+ "Por favor crea una nueva medida y ejecute de nuevo.");
					
					System.exit(-1);;
				}
				
				selection = scan.nextInt();

				if (selection <= 0 || selection > (contents + 1))
					System.out.println("Opcion incorrecta. Elija de nuevo");
				
			} while (selection <= 0 || selection > (contents + 1));
			
			measureName = options.get((selection-1));
			
			
			//Given the measure name, extract the description (if exists) of that measure
			query = "SELECT DISTINCT Description FROM Measure WHERE Measure.Name= '" + measureName + "'";
			st = conn.createStatement();
			rs = st.executeQuery(query);

			while (rs.next())
				description = rs.getString(1);
			
			//Given the measure name, extract the DAX expression of that measure
			query = "SELECT DISTINCT Expression FROM Measure WHERE Measure.Name ='" + measureName + "'";
			st = conn.createStatement();
			rs = st.executeQuery(query);

			while (rs.next())
				daxExpression = rs.getString(1);
			
			//A new measure is created with all obtained information
			m = new Measure(measureName, description, daxExpression);
			
			//A csx script will be created with all information extracted with the fetchData method
			createScript(m, date, period, measures, column);
			selection = 0;
			contents = 0;


		} catch (SQLException e) {

			e.printStackTrace();
		}

	}
	
	/**
	 * Creates a CSX script which contains the C# syntax of the creation of new measures to be executed in Tabular editor.
	 * 
	 * @param baseMeasure the original measure to be used as a template for the new time measures
	 * @param dateDim the dates dimension
	 * @param periodDim the periods dimension
	 * @param measureT the measure table
	 * @param dateID the dates ID column
	 */
	private void createScript(Measure baseMeasure, String dateDim, String periodDim, String measureT, String dateID) {
		
		//A new MeasureList object with all the time measures created from the original measure 
		MeasureList list = new MeasureList(baseMeasure, dateDim, periodDim, dateID);
		//An index to identify the new measures
		int index = 0;
		
		try {
			//File creation in the corresponding directory
			File script = new File("C:\\easyM\\script.csx");
			//OutputStreamWriter object to write on a file with a UTF_8 encoding
			OutputStreamWriter ow = new OutputStreamWriter(new FileOutputStream(script), StandardCharsets.UTF_8);
			//BufferedWriter object to write strings onto a file
			BufferedWriter bf = new BufferedWriter(ow);
			
			//For each time measure included in the time measure list
			for (Measure medida : list) {
				
				index++;
				//Writing the CSX script with C# syntax
				bf.write("var newMeasure"+index+" = Model.Tables["+"\""+measureT+"\"].AddMeasure("+
				"\""+medida.getName()+"\", "+"\""+medida.getExpression()+"\", "+"\""+baseMeasure.getName()+"\");");
				
				bf.newLine();
				bf.write("newMeasure"+index+".FormatString = \"0.00\";");
				bf.newLine();
				bf.write("newMeasure"+index+".Description = "+"\""+ medida.getDescription() +"\""+ ";");
				bf.newLine();
				bf.newLine();

			}
			//Closing the stream
			bf.close();
			
			//Object RunBatch to run a batch file with the script created
			RunBatch.run();

		} catch (IOException e) {

			e.printStackTrace();
		}
		
	}
	/**
	 * Closes the connection with the SQLite database of the model
	 */
	public void disconnect() {

		ReadOpenModels.close();

	}


}
