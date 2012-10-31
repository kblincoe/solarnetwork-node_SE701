/* ===================================================================
 * Dao.java
 * 
 * Created Nov 30, 2009 4:56:25 PM
 * 
 * Copyright 2007-2009 SolarNetwork.net Dev Team
 * 
 * This program is free software; you can redistribute it and/or 
 * modify it under the terms of the GNU General Public License as 
 * published by the Free Software Foundation; either version 2 of 
 * the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, 
 * but WITHOUT ANY WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU 
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License 
 * along with this program; if not, write to the Free Software 
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 
 * 02111-1307 USA
 * ===================================================================
 * $Id$
 * ===================================================================
 */

package net.solarnetwork.node.dao;

import java.util.List;

import net.solarnetwork.node.Datum;
import net.solarnetwork.node.DatumUpload;

/**
 * Data Access Object (DAO) API for {@link Datum} objects.
 *
 * @author matt
 * @version $Revision$ $Date$
 * @param <T> the type of Datum this DAO supports
 */
public interface DatumDao<T extends Datum> {

	/**
	 * Get the class supported by this Dao.
	 * 
	 * @return class
	 */
	Class<? extends T> getDatumType();

	/*
	 * Get a datum by its primary key.
	 * 
	 * @param id the primary key
	 * @return the datum, or <em>null</em> if not found
	 *
	T getDatum(Long id);
	*/
	
	/**
	 * Store (create or update) a datum and return it's primary key.
	 * 
	 * @param datum the datum to persist
	 * @return the generated primary key
	 */
	Long storeDatum(T datum);

	/**
	 * Get a List of Datum instances that have not been uploaded yet to a 
	 * specific destination.
	 * 
	 * <p>This does not need to return all data, it can limit the amount
	 * returned at one time to conserve memory. This method can be called
	 * repeatedly if needed.</p>
	 * 
	 * @param destination the destination to check
	 * @return list of Datum, or empty List if none available
	 */
	List<T> getDatumNotUploaded(String destination);

	/**
	 * Persist a {@link DatumUpload} instance.
	 * 
	 * @param datum the Datum that has been uploaded successfully
	 * @param destination the destination the Datum was uploaded to
	 * @param trackingId the remote tracking ID assigned to the uploaded Datum
	 * @return new DatumUpload instance
	 */
	DatumUpload storeDatumUpload(T datum, String destination, Long trackingId);
	
	/**
	 * Delete both Datum and DatumUpload objects that have been successfully
	 * uploaded to at least one destination and are older than the specified
	 * number of hours.
	 * 
	 * <p>This is designed to free up space from local database storage
	 * for devices with limited storage capacity. It will not delete any
	 * Datum objects that have not been successfully uploaded anywhere.</p>
	 * 
	 * @param hours the minimum number of hours old the data must be to delete
	 * @return the number of Datum (and associated DatumUpload) entities deleted
	 */
	int deleteUploadedDataOlderThan(int hours);
	
}
