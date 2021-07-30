package org.Measure;

/**
 * The Measure class for Power BI measure objects specification
 * 
 * @author Ángel Ciudad Montalbán
 * @since 2021
 */
public class Measure {
	/** The name of a measure*/
	private String name;
	/** The description of a measure*/
	private String description;
	/** The DAX expression of a measure*/
	private String expression;
	
	/**
	 * The main constructor
	 * 
	 * @param name
	 * @param description
	 * @param expression
	 */
	public Measure(String name, String description, String expression) {
		
		
		this.name = name;
		//In order to avoid reading: "null" in double quote
		if(description == null)
			this.description = null;	
		else
			this.description = description;
		
		
		this.expression = expression;		
		
	}
	
	/**
	 * Constructor to verify if the measure is not a time measure
	 * 
	 * @param name the name of the measure
	 */
	public Measure(String name) {
		
		this.name = name;
		this.description = null;
		this.expression = null;
		
	}

	/**
	 * Getter for the measure name
	 * @return the measure name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Setter for the measure name
	 * @param name the measure name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Getter for the measure description
	 * @return the measure description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Setter for the measure description
	 * @param name the measure description
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Getter for the measure DAX expression
	 * @return the measure DAX expression
	 */
	public String getExpression() {
		return expression;
	}

	/**
	 * Setter for the measure DAX expression
	 * @param name the measure DAX expression
	 */
	public void setExpression(String expression) {
		this.expression = expression;
	}

	/**
	 * Detects if it is a time measure
	 * 
	 * @return true if is a time measure and false if it is not.
	 */
	public boolean isTimeMeasure() {
			
			if(this.name.contains("LY") || this.name.contains("Q-1") || this.name.contains("M-1") || this.name.contains("YoY") ||
					this.name.contains("QoQ") || this.name.contains("MoM") || this.name.contains("SPLY") || this.name.contains("Rolling12M") ||
					this.name.contains("YTD") || this.name.contains("QTD") || this.name.contains("MTD") || this.name.contains("KPI")) {
				
				return true;
			}
			
			return false;
		}
}
