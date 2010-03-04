/*
 * Copyright 2010 Mozzart
 *
 *
 * This file is part of mozzes.
 *
 * mozzes is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mozzes is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with mozzes.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
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
