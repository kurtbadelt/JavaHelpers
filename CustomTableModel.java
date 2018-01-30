package Helpers;

import javax.swing.table.DefaultTableModel;

/**
 * Clase auxiliar que construye un modelo personalizado para una JTable.
 * @author poweruser
 *
 */
public class CustomTableModel extends DefaultTableModel {
	
/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final int Row = 0;

public CustomTableModel(final String [] columnas){
	super(columnas,CustomTableModel.Row);
	
}

}
