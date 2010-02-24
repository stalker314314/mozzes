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
