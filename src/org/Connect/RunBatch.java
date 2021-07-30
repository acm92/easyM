package org.Connect;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * The RunBatch class that will run the batch file that will be executed in Microsoft Power BI
 * 
 * @author Ángel Ciudad Montalbán
 * @since 2021
 */
public class RunBatch {
	
	/**
	 * It contains the needed information to run the batch file.
	 */
	public static void run() {
		try {
			//The batch file that will be executed
			String bat = "C:\\easyM\\easyMScript.bat";
			//The C# script that will be executed in Tabular Editor 
			String script = "C:\\easyM\\script.csx";
			//The server on which the model is hosted
			String server = "localhost:"+ReadOpenModels.getLocalHost();
			//The local database file included on the AnalysisServices local directory
			String database = ReadOpenModels.getDatabase();
			//The main method that will run the batch file
			runBatch(bat, server, database, script);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	/**
	 * It will run the batch file linked to Tabular Editor through the command line interface 
	 * 
	 * @param a the batch file
	 * @param b the local server
	 * @param c the database folder
	 * @param d the C# script
	 * @throws IOException if an Input/Output exception is encountered
	 * @throws InterruptedException if the thread is unexpectedly interrupted
	 */
	private static void runBatch(String a, String b, String c, String d) throws IOException, InterruptedException {
		//ProcessBuilder is to handle system processes, given a set of parameters
		ProcessBuilder pt = new ProcessBuilder(a, b, c, d);
		//In order to correlate error messages with the corresponding standard output, 
		//it redirects to the same stream
		pt.redirectErrorStream(true);
		try {
			//The process starts
			Process p = pt.start();
			//Reading the output by the new process
			BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String l;
			while ((l = r.readLine()) != null) {
				System.out.println(l);
			}
		} catch (IOException e) {
			e.printStackTrace();
		
		}
	}
	
}
