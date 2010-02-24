package org.mozzes.swing.utils;

import javax.swing.DefaultListSelectionModel;
import javax.swing.ListSelectionModel;

/**
 * 
 * @author nikolad
 *
 */
public class ToggleListSelectionModel extends DefaultListSelectionModel {

	private static final long serialVersionUID = 6520009398373728345L;

	@Override
	public void setSelectionInterval(int index0, int index1) {
		
		if (isSelectedIndex(index0)) 
			super.removeSelectionInterval(index0, index1);
		 else {
	      if(getSelectionMode() == ListSelectionModel.SINGLE_SELECTION) {
	    	  super.setSelectionInterval(index0, index1);
	      }else 
			  super.addSelectionInterval(index0, index1);
		 }
			
	}
}
