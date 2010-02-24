package org.mozzes.swing.component.table;

import javax.swing.table.TableCellRenderer;

/**
 * [AMD-58]
 * Apstraktni table cell renderer dekorator. Dodaje svoju dekoraciju
 * na postojecji table cell renderer u toku izvrshavanja.
 * 
 * @author nenadl
 *
 */
abstract class TableCellRendererDecorator implements TableCellRenderer {
    
	/**
	 * Postojecji table cell renderer koji treba dekorisati.
	 */
	protected TableCellRenderer cellRendererToDecorate; 
 
	public TableCellRendererDecorator(TableCellRenderer cellRendererToDecorate) {
        this.cellRendererToDecorate = cellRendererToDecorate;
    }
}
