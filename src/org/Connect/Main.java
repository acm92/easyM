package org.Connect;

/**
 * The main Class
 * 
 * @author Ángel Ciudad Montalbán
 * @since 2021
 */
public class Main {

public static void main (String[] args) {
		
		//Creating the DataProcess object
		DataProcess jd = new DataProcess();
		//Establishing connection to a selected model
		jd.connect();
		//Tables specification, csx script creation and batch file execution
		jd.fetchData();
		//Terminating model connection
		jd.disconnect();
		
		} 
}
