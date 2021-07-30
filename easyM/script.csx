var newMeasure1 = Model.Tables["Medidas"].AddMeasure("Ventas LY", "CALCULATE([Ventas],ALL(dPeriodo),DATEADD(dFecha[Fecha],-1,YEAR))", "Ventas");
newMeasure1.FormatString = "0.00";
newMeasure1.Description = "Ventas en el año anterior";

var newMeasure2 = Model.Tables["Medidas"].AddMeasure("Ventas Q-1", "CALCULATE([Ventas],ALL(dPeriodo),DATEADD(dFecha[Fecha],-1,QUARTER))", "Ventas");
newMeasure2.FormatString = "0.00";
newMeasure2.Description = "Ventas en el trimestre anterior";

var newMeasure3 = Model.Tables["Medidas"].AddMeasure("Ventas M-1", "CALCULATE([Ventas],ALL(dPeriodo),DATEADD(dFecha[Fecha],-1,MONTH))", "Ventas");
newMeasure3.FormatString = "0.00";
newMeasure3.Description = "Ventas en el mes anterior";

var newMeasure4 = Model.Tables["Medidas"].AddMeasure("Ventas YoY", "[Ventas]-[Ventas LY]", "Ventas");
newMeasure4.FormatString = "0.00";
newMeasure4.Description = "Diferencia de Ventas respecto al año anterior";

var newMeasure5 = Model.Tables["Medidas"].AddMeasure("Ventas QoQ", "[Ventas]-[Ventas Q-1]", "Ventas");
newMeasure5.FormatString = "0.00";
newMeasure5.Description = "Diferencia de Ventas respecto al trimestre anterior";

var newMeasure6 = Model.Tables["Medidas"].AddMeasure("Ventas MoM", "[Ventas]-[Ventas M-1]", "Ventas");
newMeasure6.FormatString = "0.00";
newMeasure6.Description = "Diferencia de Ventas respecto al mes anterior";

var newMeasure7 = Model.Tables["Medidas"].AddMeasure("Ventas % YoY", "DIVIDE([Ventas YoY],[Ventas LY])", "Ventas");
newMeasure7.FormatString = "0.00";
newMeasure7.Description = "Diferencia porcentual de Ventas respecto al año anterior";

var newMeasure8 = Model.Tables["Medidas"].AddMeasure("Ventas % QoQ", "DIVIDE([Ventas QoQ],[Ventas Q-1])", "Ventas");
newMeasure8.FormatString = "0.00";
newMeasure8.Description = "Diferencia porcentual de Ventas respecto al trimestre anterior";

var newMeasure9 = Model.Tables["Medidas"].AddMeasure("Ventas % MoM", "DIVIDE([Ventas MoM],[Ventas M-1])", "Ventas");
newMeasure9.FormatString = "0.00";
newMeasure9.Description = "Diferencia porcentual de Ventas respecto al mes anterior";

var newMeasure10 = Model.Tables["Medidas"].AddMeasure("Ventas SPLY", "CALCULATE([Ventas],SAMEPERIODLASTYEAR(dFecha[Fecha]))", "Ventas");
newMeasure10.FormatString = "0.00";
newMeasure10.Description = "Ventas en el mismo periodo del año anterior";

var newMeasure11 = Model.Tables["Medidas"].AddMeasure("Ventas YoY SPLY", "[Ventas]-[Ventas SPLY]", "Ventas");
newMeasure11.FormatString = "0.00";
newMeasure11.Description = "Diferencia de Ventas respecto al mismo periodo del año anterior";

var newMeasure12 = Model.Tables["Medidas"].AddMeasure("Ventas % YoY SPLY", "DIVIDE([Ventas YoY SPLY],[Ventas SPLY])", "Ventas");
newMeasure12.FormatString = "0.00";
newMeasure12.Description = "Diferencia porcentual de Ventas respecto al mismo periodo del año anterior";

var newMeasure13 = Model.Tables["Medidas"].AddMeasure("Ventas Rolling12M", "CALCULATE([Ventas], DATESBETWEEN(dFecha[Fecha], NEXTDAY(SAMEPERIODLASTYEAR(LASTDATE(dFecha[Fecha]))),LASTDATE(dFecha[Fecha])))", "Ventas");
newMeasure13.FormatString = "0.00";
newMeasure13.Description = "Interanual de Ventas (Rolling Year)";

var newMeasure14 = Model.Tables["Medidas"].AddMeasure("Ventas YTD", "TOTALYTD([Ventas],dFecha[Fecha])", "Ventas");
newMeasure14.FormatString = "0.00";
newMeasure14.Description = "Ventas anual acumulado";

var newMeasure15 = Model.Tables["Medidas"].AddMeasure("Ventas QTD", "TOTALQTD([Ventas],dFecha[Fecha])", "Ventas");
newMeasure15.FormatString = "0.00";
newMeasure15.Description = "Ventas trimestral acumulado";

var newMeasure16 = Model.Tables["Medidas"].AddMeasure("Ventas MTD", "TOTALMTD([Ventas],dFecha[Fecha])", "Ventas");
newMeasure16.FormatString = "0.00";
newMeasure16.Description = "Ventas mensual acumulado";

var newMeasure17 = Model.Tables["Medidas"].AddMeasure("Ventas LY MTD", "TOTALMTD([Ventas LY],dFecha[Fecha])", "Ventas");
newMeasure17.FormatString = "0.00";
newMeasure17.Description = "Ventas mensual acumulado en el año anterior";

var newMeasure18 = Model.Tables["Medidas"].AddMeasure("Ventas LY QTD", "TOTALQTD([Ventas LY],dFecha[Fecha])", "Ventas");
newMeasure18.FormatString = "0.00";
newMeasure18.Description = "Ventas trimestral acumulado en el año anterior";

var newMeasure19 = Model.Tables["Medidas"].AddMeasure("Ventas LY YTD", "TOTALYTD([Ventas LY],dFecha[Fecha])", "Ventas");
newMeasure19.FormatString = "0.00";
newMeasure19.Description = "Ventas anual acumulado en el año anterior";

var newMeasure20 = Model.Tables["Medidas"].AddMeasure("Ventas MoM MTD", "TOTALMTD([Ventas MoM],dFecha[Fecha])", "Ventas");
newMeasure20.FormatString = "0.00";
newMeasure20.Description = "Diferencia de Ventas acumulado respecto del mes anterior";

var newMeasure21 = Model.Tables["Medidas"].AddMeasure("Ventas QoQ QTD", "TOTALQTD([Ventas QoQ],dFecha[Fecha])", "Ventas");
newMeasure21.FormatString = "0.00";
newMeasure21.Description = "Diferencia de Ventas acumulado respecto del trimestre anterior";

var newMeasure22 = Model.Tables["Medidas"].AddMeasure("Ventas YoY YTD", "TOTALYTD([Ventas YoY],dFecha[Fecha])", "Ventas");
newMeasure22.FormatString = "0.00";
newMeasure22.Description = "Diferencia de Ventas acumulado respecto del año anterior";

var newMeasure23 = Model.Tables["Medidas"].AddMeasure("Ventas % MoM MTD", "DIVIDE([Ventas MoM MTD],[Ventas LY MTD])", "Ventas");
newMeasure23.FormatString = "0.00";
newMeasure23.Description = "Diferencia porcentual de Ventas acumulado respecto del mes anterior";

var newMeasure24 = Model.Tables["Medidas"].AddMeasure("Ventas % QoQ QTD", "DIVIDE([Ventas QoQ QTD],[Ventas LY QTD])", "Ventas");
newMeasure24.FormatString = "0.00";
newMeasure24.Description = "Diferencia porcentual de Ventas acumulado respecto del trimestre anterior";

var newMeasure25 = Model.Tables["Medidas"].AddMeasure("Ventas % YoY YTD", "DIVIDE([Ventas YoY YTD],[Ventas LY YTD])", "Ventas");
newMeasure25.FormatString = "0.00";
newMeasure25.Description = "Diferencia porcentual de Ventas acumulado respecto del año anterior";

var newMeasure26 = Model.Tables["Medidas"].AddMeasure("Ventas YTD SPLY", "CALCULATE([Ventas YTD],SAMEPERIODLASTYEAR(dFecha[Fecha]))", "Ventas");
newMeasure26.FormatString = "0.00";
newMeasure26.Description = "Ventas acumulado en el mismo periodo del año anterior";

var newMeasure27 = Model.Tables["Medidas"].AddMeasure("Ventas YoY YTD SPLY", "[Ventas YTD]-[Ventas YTD SPLY]", "Ventas");
newMeasure27.FormatString = "0.00";
newMeasure27.Description = "Diferencia de Ventas acumulado respecto al mismo periodo del año anterior";

var newMeasure28 = Model.Tables["Medidas"].AddMeasure("Ventas % YoY YTD SPLY", "DIVIDE([Ventas YoY YTD SPLY],[Ventas YTD SPLY])", "Ventas");
newMeasure28.FormatString = "0.00";
newMeasure28.Description = "Diferencia porcentual de Ventas acumulado respecto al mismo periodo del año anterior";

var newMeasure29 = Model.Tables["Medidas"].AddMeasure("Ventas KPI", "IF([Ventas YoY YTD],SWITCH(TRUE(),[Ventas % YoY YTD]>0,1,[Ventas % YoY YTD]<0,3,2),BLANK())", "Ventas");
newMeasure29.FormatString = "0.00";
newMeasure29.Description = "Indicador clave de rendimiento de Ventas";

