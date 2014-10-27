/* ==================================================================
 * DeviceInfoDatumDataSourceTest.java - Oct 2, 2011 9:24:34 PM
 * 
 * Copyright 2007-2011 SolarNetwork.net Dev Team
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
 * ==================================================================
 */

package net.solarnetwork.node.power.enasolar.ws.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.annotation.Resource;
import net.solarnetwork.node.domain.GeneralNodeEnergyDatum;
import net.solarnetwork.node.power.enasolar.ws.DeviceInfoDatumDataSource;
import net.solarnetwork.node.test.AbstractNodeTransactionalTest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;

/**
 * Test case for the {@link DeviceInfoDatumDataSource} class.
 * 
 * @author matt
 * @version 1.1
 */
@ContextConfiguration
public class DeviceInfoDatumDataSourceTest extends AbstractNodeTransactionalTest {

	@Resource(name = "datumDataSource")
	private DeviceInfoDatumDataSource dataSource;

	@Resource(name = "datumDataSourceList")
	private DeviceInfoDatumDataSource dataSourceList;

	@Before
	public void setup() {
		Map<String, String> deviceInfoMap = new LinkedHashMap<String, String>(10);
		deviceInfoMap.put("outputPower", "//data[@key='acPower']/@value");
		deviceInfoMap.put("decaWattHoursTotal", "//data[@key='decaWattHoursTotal']/@value");
		dataSource.setXpathMap(deviceInfoMap);

		Map<String, String> metersDataMap = new LinkedHashMap<String, String>(10);
		metersDataMap.put("outputPower", "//OutputPower");
		metersDataMap.put("energyLifetime", "//EnergyLifetime");
		dataSourceList.setXpathMap(metersDataMap);
	}

	@Test
	public void parseDeviceInfoDatum() {
		GeneralNodeEnergyDatum datum = dataSource.readCurrentDatum();
		log.debug("Got datum: {}", datum);
		assertEquals(Long.valueOf(57540), datum.getWattHourReading());
		assertNotNull(datum.getWatts());
		assertEquals(Integer.valueOf(628), datum.getWatts());
	}

	@Test
	public void parseMetersDataDatum() {
		GeneralNodeEnergyDatum datum = dataSourceList.readCurrentDatum();
		log.debug("Got datum: {}", datum);
		assertEquals(Integer.valueOf(214), datum.getWatts());
		assertEquals(Long.valueOf(7629660), datum.getWattHourReading());
	}
}
