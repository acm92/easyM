package org.Measure;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * The MeasureList class that creates a ArrayList-based list which stores Measure objects
 * Implements the Iterable interface in order to use a foreach loop. 
 * 
 * @author Ángel Ciudad Montalbán
 * @since 2021
 */
public class MeasureList implements Iterable<Measure> {
	/** The dates dimension of the dimensional model  */
	private String datesDimensionName;
	/** The date periods of the dimensional model  */
	private String periodsDimensionName;
	/** The main column of the dates dimension  */
	private String datesIDName;
	/** An ArrayList of Measure objects  */
	private ArrayList<Measure> list;
	
	/**
	 * The main constructor. Given a Measure object and the key values of the dimensions, it creates
	 * a list of time measures
	 * 
	 * @param m the original measure on which the time measures will be based
	 * @param datesName the dates dimension name
	 * @param periodsName the periods dimension name
	 * @param datesIDName the main dates dimension column
	 */
	public MeasureList(Measure m, String datesName, String periodsName, String datesID) {
		
		this.datesDimensionName = datesName;
		this.periodsDimensionName = periodsName;
		this.datesIDName = datesID;
		list = createTimeMeasures(m);
		
	}
	/**
	 * Creates an ArrayList of time measures based on the original given measure
	 * 
	 * @param m the original measure
	 * @return an ArrayList object with all time measures
	 */
	private ArrayList<Measure> createTimeMeasures(Measure m) {
		//ArrayList which will store the measures
		ArrayList<Measure> listaAux = new ArrayList<Measure>();
		
		
		Measure LY = new Measure(m.getName()+" LY", m.getName() + " en el año anterior", "CALCULATE(["+m.getName()+"],ALL("+periodsDimensionName+"),DATEADD("+datesDimensionName+"["+datesIDName+"],-1,YEAR))");
		Measure Qminus1 = new Measure(m.getName()+" Q-1", m.getName() + " en el trimestre anterior", "CALCULATE(["+m.getName()+"],ALL("+periodsDimensionName+"),DATEADD("+datesDimensionName+"["+datesIDName+"],-1,QUARTER))");
		Measure Mminus1 = new Measure(m.getName()+" M-1", m.getName() + " en el mes anterior", "CALCULATE(["+m.getName()+"],ALL("+periodsDimensionName+"),DATEADD("+datesDimensionName+"["+datesIDName+"],-1,MONTH))");
		Measure YoY = new Measure(m.getName()+" YoY", "Diferencia de "+ m.getName()+" respecto al año anterior", "["+m.getName()+"]-["+LY.getName()+"]");
		Measure QoQ = new Measure(m.getName()+" QoQ", "Diferencia de "+ m.getName()+" respecto al trimestre anterior", "["+m.getName()+"]-["+Qminus1.getName()+"]");
		Measure MoM = new Measure(m.getName()+" MoM", "Diferencia de "+ m.getName()+" respecto al mes anterior", "["+m.getName()+"]-["+Mminus1.getName()+"]");
		Measure PercentYoY = new Measure(m.getName()+" % YoY", "Diferencia porcentual de "+ m.getName()+" respecto al año anterior", "DIVIDE(["+YoY.getName()+"],["+LY.getName()+"])");
		Measure PercentQoQ = new Measure(m.getName()+" % QoQ", "Diferencia porcentual de "+ m.getName()+" respecto al trimestre anterior", "DIVIDE(["+QoQ.getName()+"],["+Qminus1.getName()+"])");
		Measure PercentMoM = new Measure(m.getName()+" % MoM", "Diferencia porcentual de "+ m.getName()+" respecto al mes anterior", "DIVIDE(["+MoM.getName()+"],["+Mminus1.getName()+"])");
		Measure SPLY = new Measure(m.getName()+" SPLY", m.getName()+" en el mismo periodo del año anterior", "CALCULATE(["+m.getName()+"],SAMEPERIODLASTYEAR("+datesDimensionName+"["+datesIDName+"]))");
		Measure YoYSPLY = new Measure(m.getName()+" YoY SPLY", "Diferencia de "+ m.getName()+" respecto al mismo periodo del año anterior", "["+m.getName()+"]-["+SPLY.getName()+"]");
		Measure PercentYoYSPLY = new Measure(m.getName()+" % YoY SPLY", "Diferencia porcentual de "+ m.getName()+" respecto al mismo periodo del año anterior", "DIVIDE(["+YoYSPLY.getName()+"],["+SPLY.getName()+"])");
		Measure Rolling12M = new Measure(m.getName()+" Rolling12M", "Interanual de "+m.getName()+ " (Rolling Year)", "CALCULATE(["+m.getName()+"], DATESBETWEEN("+datesDimensionName+"["+datesIDName+"], NEXTDAY(SAMEPERIODLASTYEAR(LASTDATE("+datesDimensionName+"["+datesIDName+"]))),LASTDATE("+datesDimensionName+"["+datesIDName+"])))");
		Measure YTD = new Measure(m.getName()+" YTD", m.getName()+ " anual acumulado", "TOTALYTD(["+m.getName()+"],"+datesDimensionName+"["+datesIDName+"])"); 
		Measure QTD = new Measure(m.getName()+" QTD", m.getName()+ " trimestral acumulado", "TOTALQTD(["+m.getName()+"],"+datesDimensionName+"["+datesIDName+"])"); 
		Measure MTD = new Measure(m.getName()+" MTD", m.getName()+ " mensual acumulado", "TOTALMTD(["+m.getName()+"],"+datesDimensionName+"["+datesIDName+"])"); 
		Measure LYMTD = new Measure(m.getName()+" LY MTD", m.getName()+ " mensual acumulado en el año anterior", "TOTALMTD(["+LY.getName()+"],"+datesDimensionName+"["+datesIDName+"])"); 
		Measure LYQTD  = new Measure(m.getName()+" LY QTD", m.getName()+ " trimestral acumulado en el año anterior", "TOTALQTD(["+LY.getName()+"],"+datesDimensionName+"["+datesIDName+"])"); 
		Measure LYYTD = new Measure(m.getName()+" LY YTD", m.getName()+ " anual acumulado en el año anterior", "TOTALYTD(["+LY.getName()+"],"+datesDimensionName+"["+datesIDName+"])"); 
		Measure MoMMTD = new Measure(m.getName()+" MoM MTD","Diferencia de " + m.getName()+ " acumulado respecto del mes anterior", "TOTALMTD(["+MoM.getName()+"],"+datesDimensionName+"["+datesIDName+"])"); 
		Measure QoQQTD  = new Measure(m.getName()+" QoQ QTD", "Diferencia de " + m.getName()+ " acumulado respecto del trimestre anterior", "TOTALQTD(["+QoQ.getName()+"],"+datesDimensionName+"["+datesIDName+"])"); 
		Measure YoYYTD = new Measure(m.getName()+" YoY YTD", "Diferencia de " + m.getName()+ " acumulado respecto del año anterior", "TOTALYTD(["+YoY.getName()+"],"+datesDimensionName+"["+datesIDName+"])"); 
		Measure PercentMoM_MTD = new Measure(m.getName()+" % MoM MTD", "Diferencia porcentual de " + m.getName()+ " acumulado respecto del mes anterior", "DIVIDE(["+MoMMTD.getName()+"],["+LYMTD.getName()+"])");
		Measure PercentQoQ_QTD = new Measure(m.getName()+" % QoQ QTD", "Diferencia porcentual de " + m.getName()+ " acumulado respecto del trimestre anterior", "DIVIDE(["+QoQQTD.getName()+"],["+LYQTD.getName()+"])");
		Measure PercentYoY_YTD = new Measure(m.getName()+" % YoY YTD", "Diferencia porcentual de " + m.getName()+ " acumulado respecto del año anterior", "DIVIDE(["+YoYYTD.getName()+"],["+LYYTD.getName()+"])");
		Measure YTDSPLY = new Measure(m.getName()+" YTD SPLY", m.getName()+ " acumulado en el mismo periodo del año anterior", "CALCULATE(["+YTD.getName()+"],SAMEPERIODLASTYEAR("+datesDimensionName+"["+datesIDName+"]))");
		Measure YoY_YTD_SPLY = new Measure(m.getName()+" YoY YTD SPLY", "Diferencia de " + m.getName()+ " acumulado respecto al mismo periodo del año anterior", "["+YTD.getName()+"]-["+YTDSPLY.getName()+"]");
		Measure PercentYoY_YTD_SPLY = new Measure(m.getName()+" % YoY YTD SPLY", "Diferencia porcentual de " + m.getName()+ " acumulado respecto al mismo periodo del año anterior", "DIVIDE(["+YoY_YTD_SPLY.getName()+"],["+YTDSPLY.getName()+"])");
		Measure KPI = new Measure(m.getName()+" KPI", "Indicador clave de rendimiento de " + m.getName(), "IF(["+YoYYTD.getName()+"],SWITCH(TRUE(),["+PercentYoY_YTD.getName()+"]>0,1,["+PercentYoY_YTD.getName()+"]<0,3,2),BLANK())");
		
		
		
		//All measures will be added to the list
		listaAux.add(LY);
		listaAux.add(Qminus1);
		listaAux.add(Mminus1);
		listaAux.add(YoY);
		listaAux.add(QoQ);
		listaAux.add(MoM);
		listaAux.add(PercentYoY);
		listaAux.add(PercentQoQ);
		listaAux.add(PercentMoM);
		listaAux.add(SPLY);
		listaAux.add(YoYSPLY);
		listaAux.add(PercentYoYSPLY);
		listaAux.add(Rolling12M);
		listaAux.add(YTD);
		listaAux.add(QTD);
		listaAux.add(MTD);
		listaAux.add(LYMTD);
		listaAux.add(LYQTD);
		listaAux.add(LYYTD);
		listaAux.add(MoMMTD);
		listaAux.add(QoQQTD);
		listaAux.add(YoYYTD);
		listaAux.add(PercentMoM_MTD);
		listaAux.add(PercentQoQ_QTD);
		listaAux.add(PercentYoY_YTD);
		listaAux.add(YTDSPLY);
		listaAux.add(YoY_YTD_SPLY);
		listaAux.add(PercentYoY_YTD_SPLY);
		listaAux.add(KPI);
		
		
		return listaAux;
	}

	/**
	 * The iterator method implemented from Iterable
	 */
	@Override
	public Iterator<Measure> iterator() {
		return list.iterator();
	}


	
	
	
	

}
