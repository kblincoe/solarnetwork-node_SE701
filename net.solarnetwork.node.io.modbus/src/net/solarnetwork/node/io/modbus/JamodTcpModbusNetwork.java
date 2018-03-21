/* ==================================================================
 * JamodTcpModbusNetwork.java - 3/02/2018 7:58:36 AM
 * 
 * Copyright 2018 SolarNetwork.net Dev Team
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

package net.solarnetwork.node.io.modbus;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import net.solarnetwork.node.settings.SettingSpecifier;
import net.solarnetwork.node.settings.SettingSpecifierProvider;
import net.solarnetwork.node.settings.support.BasicTextFieldSettingSpecifier;
import net.wimpi.modbus.net.TCPMasterConnection;

/**
 * Jamod implementation of {@link ModbusNetwork} using a TCP connection.
 * 
 * @author matt
 * @version 1.0
 * @since 2.4
 */
public class JamodTcpModbusNetwork extends AbstractModbusNetwork implements SettingSpecifierProvider {

	private String host;
	private int port = net.wimpi.modbus.Modbus.DEFAULT_PORT;

	/**
	 * Default constructor.
	 */
	public JamodTcpModbusNetwork() {
		super();
		setUid("Modbus TCP");
	}

	@Override
	public <T> T performAction(ModbusConnectionAction<T> action, int unitId) throws IOException {
		ModbusConnection conn = null;
		try {
			conn = createConnection(unitId);
			conn.open();
			return action.doWithConnection(conn);
		} finally {
			if ( conn != null ) {
				try {
					conn.close();
				} catch ( RuntimeException e ) {
					// ignore this
				}
			}
		}
	}

	@Override
	public ModbusConnection createConnection(int unitId) {
		try {
			TCPMasterConnection conn = new LockingTcpConnection(InetAddress.getByName(host));
			conn.setPort(port);
			conn.setTimeout((int) getTimeoutUnit().toMillis(getTimeout()));
			return new JamodTcpModbusConnection(conn, unitId);
		} catch ( UnknownHostException e ) {
			throw new RuntimeException("Unknown modbus host [" + host + "]");
		}
	}

	@Override
	protected String getNetworkDescription() {
		return host + ":" + port;
	}

	/**
	 * Internal extension of {@link TCPMasterConnection} that utilizes a
	 * {@link Lock} to serialize access to the connection between threads.
	 */
	private class LockingTcpConnection extends TCPMasterConnection {

		/**
		 * Constructor.
		 * 
		 * @param addr
		 *        the host address
		 */
		private LockingTcpConnection(InetAddress addr) {
			super(addr);
		}

		@Override
		public void connect() throws Exception {
			if ( !isConnected() ) {
				acquireLock();
				super.connect();
			}
		}

		@Override
		public void close() {
			try {
				if ( isConnected() ) {
					super.close();
				}
			} finally {
				releaseLock();
			}
		}

		@Override
		protected void finalize() throws Throwable {
			releaseLock(); // as a catch-all
			super.finalize();
		}
	}

	// SettingSpecifierProvider

	@Override
	public String getSettingUID() {
		return "net.solarnetwork.node.io.modbus.tcp";
	}

	@Override
	public String getDisplayName() {
		return "Modbus TCP port";
	}

	@Override
	public List<SettingSpecifier> getSettingSpecifiers() {
		JamodTcpModbusNetwork defaults = new JamodTcpModbusNetwork();
		List<SettingSpecifier> results = new ArrayList<SettingSpecifier>(5);
		results.add(new BasicTextFieldSettingSpecifier("uid", String.valueOf(defaults.getUid())));
		results.add(new BasicTextFieldSettingSpecifier("host", defaults.host));
		results.add(new BasicTextFieldSettingSpecifier("port", String.valueOf(defaults.port)));
		results.add(
				new BasicTextFieldSettingSpecifier("timeout", String.valueOf(defaults.getTimeout())));
		return results;
	}

	// Accessors

	/**
	 * Get the host to connect to.
	 * 
	 * @return the host
	 */
	public String getHost() {
		return host;
	}

	/**
	 * Set the host to connect to.
	 * 
	 * <p>
	 * This can be a host name or IPv4 or IPv6 address.
	 * </p>
	 * 
	 * @param host
	 *        the host to connect to
	 */
	public void setHost(String host) {
		this.host = host;
	}

	/**
	 * Get the network port to connect to.
	 * 
	 * @return the network port; defaults to {@literal 502}
	 */
	public int getPort() {
		return port;
	}

	/**
	 * Set the network port to connect to.
	 * 
	 * @param port
	 *        the network port
	 */
	public void setPort(int port) {
		this.port = port;
	}

}