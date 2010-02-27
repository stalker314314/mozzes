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

import java.io.*;
import java.net.*;
import java.util.*;

import org.mozzes.remoting.common.RemotingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Ovo je glavna klasa u remoting mehanizmu. Server osluskuje i prihvata konekcije na zadatom portu. Nakon uspesno
 * ostvarene konekcije pokrece se RemotingClientListener koji je zaduzen za komunikaciju sa jednim klijentom, a ovaj
 * server nastavlja da iscekuje nove konekcije.
 * 
 * @author Perica Milosevic
 * @version v1.5
 */
public class RemotingServer {

    private final Logger logger = LoggerFactory.getLogger(RemotingServer.class);

    /** Port na kome server ocekuje konekcije */
    private int port;

    /** dispatcher za kreiranje action executora */
    private RemotingActionDispatcher dispatcher = null;

    /** Thread koji osluskuje i ocekuje konekcije */
    private RemotingServerListenThread listenThread = null;

    /**
     * Kreiranje RemotingServer-a sa custom dispecerom akcija.
     * 
     * @param port port na kome ce server slusati
     */
    RemotingServer(int port) {
        this.port = port;
        this.dispatcher = new RemotingActionDispatcher();
    }

    /**
     * Metoda koja pokrece server.
     */
    public synchronized void startServer() throws IOException {
        if (listenThread == null) {
            try {
                // pokrecemo listenThread
                listenThread = new RemotingServerListenThread(port);
                listenThread.start();
            } catch (IOException ex) {
                listenThread = null;
                throw ex;
            }
        }
    }

    /**
     * Metoda koja zaustavlja server
     */
    public synchronized void stopServer() {
        if (listenThread != null) {
            listenThread.stopListening();
            listenThread = null;
        }
    }

    /**
     * Vraca port na kome radi ovaj server
     * 
     * @return port na kome radi server
     */
    public int getPort() {
        return port;
    }

    /**
     * Vraca informaciju o tome da li je server pokrenut
     * 
     * @return da li je server pokrenut?
     */
    public synchronized boolean isRunning() {
        return ((listenThread != null) && (listenThread.isRunning()));
    }

    /**
     * Vraca broj aktivnih klijenata
     * 
     * @return broj aktivnih klijenata
     */
    public synchronized int getNumberOfActiveClients() {
        if (listenThread != null)
            return listenThread.getNumberOfActiveClients();
        
        return 0;
    }

    public synchronized RemotingServer addActionMapping(RemotingActionMapping mapping) {
        dispatcher.addMapping(mapping);
        return this;
    }

    public RemotingServer addActionMapping(Properties mapping) throws RemotingException {
        return addActionMapping(new RemotingActionMapping(mapping));
    }

    /**
     * Ovaj Thread je zaduzen da osluskuje i opsluzuje zahteve za konekcijom
     */
    private class RemotingServerListenThread extends Thread {

        /** Socket na kome ocekujemo konekcije */
        private ServerSocket socketToListen = null;

        /** Ova klasa prati aktivne klijente */
        private RemotingClientManager clientManager = null;

        /** Broj klijenata koji se povezao na server u toku rada */
        private int totalClients = 0;

        RemotingServerListenThread(int port) throws IOException {
            super("RemotingServer(" + port + ")");
            this.socketToListen = new ServerSocket(port);
            this.socketToListen.setSoTimeout(0);
            this.clientManager = new RemotingClientManager();
            logger.debug("Remoting server started on port: " + port);
        }

        @Override
        public void run() {
        	try {
                while (clientManager.isActive()) {
                    Thread.yield();
                    try {
                        // ocekujemo konekciju
                        Socket clientSocket = socketToListen.accept();
                        String clientAddress = clientSocket.getInetAddress().toString() + ":" + clientSocket.getPort();
    					logger.debug("Client socket accepted: " + clientAddress);
                        RemotingClientListener clientListener = new RemotingClientListener(dispatcher, clientManager,
                                clientSocket, getName(), getClientId());

                        // ukoliko manager prihati novog klijenta pokrenucemo thread
                        if (clientManager.add(clientListener)) {
                        	logger.debug("Client socket allowed: " + clientAddress);
                            clientListener.start();
                        } else {
                        	logger.debug("Client socket not allowed: " + clientAddress);
                            try {
                                clientSocket.close();
                            } catch (IOException ignore) {
                            	logger.debug("Catched IOException while closing client socket", ignore);
                            }
                        }
                    } catch (IOException ex) {
                    	logger.debug("Catched IOException", ex);
                    }
                }
        	} catch (Throwable thr) {
        		logger.error("Unknown exception in remoting server!", thr);
        	} finally {
            	logger.info("Remoting server stopped");
        	}
        }

        /**
         * Gasenje svih klijentskih listener-a, i na kraju gasenje i ovog Thread-a
         */
        void stopListening() {
        	logger.info("Remoting server stop requested");
            // izdajemo nalog svim Thread-ovima koji rade sa
            // klijentima da se zaustave
            clientManager.stopAll();

            // zaustavljamo serverski socket
            synchronized (this) {
                if (socketToListen != null) {
                    try {
                        socketToListen.close();
                    } catch (IOException ex) {
                    }
                    socketToListen = null;
                }
            }

            // cekamo da se svi klijenti obave zaustavljanje
            clientManager.waitClientsToStop();
        }

        boolean isRunning() {
            return clientManager.isActive();
        }

        int getNumberOfActiveClients() {
            return clientManager.numberOfListeners();
        }

        private synchronized int getClientId() {
            return ++totalClients;
        }
    }
}
