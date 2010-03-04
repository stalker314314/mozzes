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
package org.mozzes.remoting.server;

import java.util.HashMap;

/**
 * Fabrika RemotingServer-a.
 * <p>
 * Ukoliko vec postoji RemotingServer pokrenut na nekom portu onda fabrika vraca postojecu instancu i ne kreira novi
 * RemotingServer. Implementacija je thread-safe
 * 
 * @author Perica Milosevic
 * @version 1.8.2
 * @see RemotingServer
 */
public class RemotingServerFactory {

    /**
     * Kreirani RemotingServer-i.
     */
    private static HashMap<Integer, RemotingServer> servers = new HashMap<Integer, RemotingServer>();

    /**
     * Kreira RemotingServer na trazenom portu ili vraca postojecu instancu RemotingServer-a. Metoda ne vodi racuna o
     * tome da li je server pokrenut ili ne, tj. ne garantuje da je server startovan, vec je ostavljeno klijentu da
     * uradi.
     * 
     * @param port Port na kome server prihvata konekcije
     * @return RemotingServer - Novi ili postojeci
     */
    public static RemotingServer getServer(int port) {
        if (port <= 0)
            return null;

        return getServer(Integer.valueOf(port));
    }

    /**
     * Isto kao i {@link #getServer(int)}
     * 
     * @param port Port na kome server prihvata konekcije
     * @return RemotingServer - Novi ili postojeci server
     * @see #getServer(int)
     */
    public static RemotingServer getServer(Integer port) {
        if (port == null || port.intValue() <= 0)
            return null;

        RemotingServer returnValue = servers.get(port);

        if (returnValue == null)
            returnValue = createServer(port);

        return returnValue;
    }

    private static synchronized RemotingServer createServer(Integer port) {
        RemotingServer returnValue = servers.get(port);
        if (returnValue == null) {
            returnValue = new RemotingServer(port.intValue());
            servers.put(port, returnValue);
        }
        return returnValue;
    }
}
