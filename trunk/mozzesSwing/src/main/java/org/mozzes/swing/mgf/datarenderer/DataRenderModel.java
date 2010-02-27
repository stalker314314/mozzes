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
package org.mozzes.swing.mgf.datarenderer;

import java.awt.Component;

import org.mozzes.swing.mgf.datamodel.DataModel;
import org.mozzes.swing.mgf.datasource.DataSource;


/**
 * Represents a render model.<br>
 * Render model combines {@link DataSource} and {@link DataModel} in order to create a component that users can put in
 * their form layout, or further customize
 * 
 * @author milos
 * 
 * @param <T> Type of the bean which this {@link DataRenderModel} works with
 * @param <D> Type of the {@link DataSource} whit which this {@link DataRenderModel} works
 */
public interface DataRenderModel<T, D extends DataSource<?>> {
	/**
	 * @return {@link DataSource} used by this {@link DataRenderModel}
	 */
	public D getDataSource();

	/**
	 * Sets the {@link DataSource} used by this {@link DataRenderModel}
	 * 
	 * @param source {@link DataSource} to be set
	 */
	public void setDataSource(D source);

	/**
	 * @return {@link DataModel} used by this {@link DataRenderModel}
	 */
	public DataModel<T> getDataModel();

	// [NOTICE] intentionally left out, RenderModel should never change its model after construction!
	// /**
	// * Sets the {@link DataModel} used by this {@link DataRenderModel}
	// *
	// * @param model {@link DataModel} to be set
	// */
	// public void setDataModel(DataModel<T> model);

	/**
	 * @return Generated {@link Component component} that you should put in your form layout
	 */
	public Component getRenderComponent();
}
