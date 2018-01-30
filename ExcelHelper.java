/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Helpers;
import java.io.FileOutputStream;


import java.util.ArrayList;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

/**
 *
 * @author kurtbadelt
 */
public class ExcelHelper {
    
    	/**
	 * Escribe los datos enviados a un archivo de Excel
	 * 
	 * @param encabezados: un vector con los encabezados a escribir en la primera l�na de la hoja de excel
	 * @param datos : un arreglo de vectores que contiene los datos a escribir
	 * @param nombre: es el nombre para el archivo, sin xls
	 * @param nombreReporte: es el nombre de la hoja
	 * @throws Exception
	 */
	public void Exportar(final ArrayList encabezados, final ArrayList[]datos,final String nombre,final String nombreReporte)throws Exception{
		final HSSFWorkbook wb = new HSSFWorkbook();
	    final FileOutputStream fileOut = new FileOutputStream(nombre+".xls");
	    final HSSFSheet sheet = wb.createSheet(nombreReporte);
	   
	    //Fila 0 Contiene los encabezados.
	    final HSSFRow row = sheet.createRow((short)0);
	    for(int i=0;i<encabezados.size();i++){
	    	 final HSSFCell cell = row.createCell((short)i);
	 	    cell.setCellValue(encabezados.get(i).toString());
	    }
	    
	    for(int i=0;i<datos.length;i++){
	    	for(int j=0;j<datos[i].size();j++){
	    		
	    		try{
	    			final double num = Double.parseDouble(datos[i].get(j).toString());
	    			final HSSFRow rowj = sheet.createRow((short)j+1);
		    		final HSSFCell celli = rowj.createCell((short)i);
		    		celli.setCellValue(num);
	    		}catch(final NumberFormatException ex){
	    			try{
	    			final HSSFRow rowj = sheet.createRow((short)j+1);
		    		final HSSFCell celli = rowj.createCell((short)i);
		    		celli.setCellValue(datos[i].get(j).toString());
	    			}catch(final Exception e){
	    				final HSSFRow rowj = sheet.createRow((short)j+1);
			    		final HSSFCell celli = rowj.createCell((short)i);
			    		celli.setCellValue("");
	    			}
	    		}
	    		
	    	}
	    }
	    wb.write(fileOut);
	    //Cerrando el archivo
	    fileOut.close();
	    
	    
		
	}
    
}
