package org.Connect;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Map.Entry;

/**
 * Class for establish connection to open PBI models
 * @author Ángel Ciudad Montalbán
 * @since 2021
 *
 */
public class ReadOpenModels {
	
	/** Connection object */
	private static Connection conn = null;
	
	/** The name/ID to the database folder of the model */
	private static String database = "";
	
	/** The absolute path to the file containing the port */
	private static String localhostFile = "";

	/** The AnalysisServices directory */
	private static String analysisServicesWorkspaceDir = System.getProperty("user.home") + File.separator + "Microsoft"
			+ File.separator + "Power BI Desktop Store App" + File.separator + "AnalysisServicesWorkspaces";

	
	/**
	 * Shows a list of all open Power BI Desktop models and connects to the one selected by the user
	 * 
	 * @throws PbixModelNotFoundException if there are no open models
	 * @throws SQLException if it was impossible to connect to any open models
	 */
	public static Connection connect() throws PbixModelNotFoundException, SQLException {
		//List all open models with a map
		//(KEY:name.pbix - VALUES: directory - database)
		Map<String, HashMap<String, String>> modelMap = listFiles();
		//If no open models, an exception is thrown
		if (modelMap.isEmpty())
			throw new PbixModelNotFoundException("No hay modelos abiertos");
		//For the user to choose one of the open models
		String[] connectionStrings = selectModel(modelMap);
		//Returns the adequate Connection object 
		return connect(modelMap.get(connectionStrings[0]));
	}
	
	/**
	 * Close all existing connections.
	 */
	public static void close() {
		try {
			conn.close();
		} catch (Exception e) {
		}
		System.out.println("Conexiones cerradas.");
	}
	
	/**
	 * Makes the connection to the previously selected open model 
	 * 
	 * @param hashMap the AnalysisServices folder and database name key-value pair
	 * @return the Connection object 
	 * @throws SQLException if a problem was encountered while trying to establish connection
	 */
	private static Connection connect(HashMap<String, String> hashMap) throws SQLException {
		//Get the number id of the AnalysisServices workspace
		ArrayList<String> list = new ArrayList<String>(hashMap.keySet());
		//Create an url of the folder which the database is stored
		String dir = analysisServicesWorkspaceDir + File.separator + "AnalysisServicesWorkspace" + list.get(0)
				+ File.separator + "Data" + File.separator;
		//Locate the file with the .db extension
		File f = new File(dir);
		File[] files = f.listFiles(new FileFilter() {

			@Override
			public boolean accept(File pathname) {
				
				return pathname.toString().endsWith(".db");
			}
		});
		//Obtain the url of said file, without the .db extension
		String aux = files[0].getName();
		database = aux.substring(aux.lastIndexOf("\\") + 1, aux.indexOf("."));
		//Locate the file including the port of the local host
		File f2 = new File(dir);
		File[] files2 = f2.listFiles(new FilenameFilter() {

			public boolean accept(File pathname, String name) {
				
				return name.startsWith("msmdsrv.port");
			}
		});
	
		localhostFile = files2[0].getAbsolutePath();
		
		//Prepare the database connection string with the jdbc-sqlite combination
		String url = "jdbc:sqlite:" + dir + files[0].getName() + File.separator + "metadata.sqlitedb";
		//Establish connection
		conn = DriverManager.getConnection(url);
		System.out.println("\n**Conexion con base de datos establecida.**\n");

		return conn;
	}
	
	/**
	 * List all open PBI models
	 * @return the map with the next data: (name.pbix - directory - database)
	 */
	private static HashMap<String, HashMap<String, String>> listFiles() {
		File f = new File(analysisServicesWorkspaceDir);
		//List all folders in the AnalysisServices directory
		File[] files = f.listFiles();
		//Map to store the (KEY:name.pbix - VALUES: directory - database) combination 
		HashMap<String, HashMap<String, String>> mapModel = new HashMap<String, HashMap<String, String>>();
		
		//Iterate through all files to find the "Data" folder
		for (File file : files) {
			File[] x = file.listFiles();
			for (File file2 : x) {
				//If the folder is found, it gets the absolute path to FlightRecorderCurrent.trc
				if (file2.getName().equals("Data")) {
					File flightRecorderFile = new File(file2, "FlightRecorderCurrent.trc");
					try {
						//Get the url of the model folder
						String url = file.getName().replace("AnalysisServicesWorkspace", "");
						//Read the file "FlightRecorderCurrent.trc" in order to find the actual .pbix name
						//and the name of its database
						readNameAndDatabase(url, flightRecorderFile, mapModel);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
		return mapModel;
	}
	/**
	 * Reads and extracts the information from the FlightRecorderCurrent.trc file to obtain the name of 
	 * the .pbix file and the name of the corresponding database. Then, such information is saved in a map
	 * 
	 * @param url the location of the AnalysisServices folder
	 * @param flightRecorderFile the FlightRecorderCurrent.trc file
	 * @param mapModel the map on which the information will be saved
	 * @throws IOException
	 */
	private static void readNameAndDatabase(String url, File flightRecorderFile,
			HashMap<String, HashMap<String, String>> mapModel) throws IOException {
		//Prefixes dictated in the FlightRecorderCurrent.trc file
		String nameStartPrefix = "<ddl700_700:PackagePath>";
		String nameEndPrefix = "</ddl700_700:PackagePath>";
		String databaseStartPrefix = "<DatabaseName>";
		String databaseEndPrefix = "</DatabaseName>";
		
		//If there is no such file, no need for search
		if (!flightRecorderFile.exists())
			return;

		BufferedReader br = new BufferedReader(new FileReader(flightRecorderFile));
		boolean flagName = true;
		boolean flagDatabase = true;
		String line;
		String key = null;
		String value;
		
		//The process ends if the name of the file and the database are found and inserted into the map.
		//If this information has not been extracted, the model is avoided and not listed.
		//The reason for that is because the model could be corrupt or an incorrect .pbix model
		while ((flagName || flagDatabase) && (line = br.readLine()) != null) {
			//line = line.replaceAll("[^a-zA-Z0-9.\\\\/<>\\-_\\:]", "");
			line = line.replaceAll("[^a-zA-Z0-9.\\\\/<>\\-_:]", "");
			if (flagName && line.contains(nameStartPrefix)) {
				//The prefix and suffix are removed, only the name of the .pbix file
				key = line.substring(line.indexOf(nameStartPrefix) + nameStartPrefix.length(),
						line.indexOf(nameEndPrefix));
				File f = new File(key);
				key = f.getName();
				flagName = false;
			}
			if (flagDatabase && line.contains(databaseStartPrefix)) {
				value = line.substring(line.indexOf(databaseStartPrefix) + databaseStartPrefix.length(),
						line.indexOf(databaseEndPrefix));
				mapModel.put(key, new HashMap<String, String>());
				mapModel.get(key).put(url, value);
				flagDatabase = false;
			}
		}
		br.close();
	}
	
	/**
	 * Displays the user different Power BI models that can connect to.
	 * 
	 * @param modelMap a map with the connection keys (name.pbix - folder - database)
	 * @return a String array with the connection keys of the selected model
	 */
	private static String[] selectModel(Map<String, HashMap<String, String>> modelMap) {
		//ArrayList for the different models on the map
		ArrayList<String> auxiliarList = new ArrayList<String>();
		//(KEY:name.pbix - VALUES: directory - database)
		for (Entry<String, HashMap<String, String>> it : modelMap.entrySet()) {
			for (String it2 : it.getValue().keySet()) {
				auxiliarList.add(it.getKey() + " - " + it2);
			}
		}

		System.out.println("Estos son los posibles modelos a los que te puedes conectar:");
		System.out.println("======================================================");
		for (int i = 0; i < auxiliarList.size(); i++) {
			System.out.println((i + 1) + ".- " + auxiliarList.get(i).split("-")[0].trim());
		}
		System.out.println("======================================================");

		int option = readOption("Introduce el modelo que quieres cargar: ", 1, auxiliarList.size(),
				"Error: Debes seleccionar una opcion dentro del rango ", "Error: Debes introducir un valor positivo");
		return auxiliarList.get(option - 1).split(" - ");
	}
	
	/**
	 * Shows the user a prompt to select a numeric value inside a range
	 * 
	 * @param message the custom message
	 * @param min the minimum value
	 * @param max the maximum value
	 * @param errorMessages the custom error message
	 * @return the integer value selected
	 */
	public static int readOption(String message, int min, int max, String... errorMessages) {
		Scanner sc = new Scanner(System.in);
		do {
			System.out.println(message);
			try {
				int option = Integer.parseInt(sc.nextLine());
				if (option < min || option > max) {
					System.out.println(errorMessages[0] + " (" + min + "-" + max + ")");
				} else {
					return option;
				}
			} catch (NumberFormatException e) {
				System.out.println(errorMessages[1]);
			}
		} while (true);
	}

	public static String getDatabase() {
		return database;
	}
	
	
	/**
	 * A Getter for the LocalHost numeric port value inside the msmdsrv.port file
	 * @return a String containing the numeric port value
	 * @throws FileNotFoundException if the msmdsrv.port file is not located  
	 */
	public static String getLocalHost() throws FileNotFoundException {

		String localhost = "";
		StringBuilder st1 = new StringBuilder();
		
		try {
			BufferedReader br = new BufferedReader(new FileReader(new File(localhostFile)));
			localhost = br.readLine();
			//Each integer value of the port is separated by unknown values that appear whitespaces
			//With this process only the digits are printed
			for(int i = 0; i < localhost.length();i++) {
				
				if(!Character.isDigit(localhost.charAt(i)))
					continue;
				
				st1.append(localhost.charAt(i));
				
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		//StringBuilder is transformed to String
		localhost = st1.toString();
		

		return localhost;
	}

}
