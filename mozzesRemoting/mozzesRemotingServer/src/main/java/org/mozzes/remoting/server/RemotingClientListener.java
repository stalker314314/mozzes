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

import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;

import org.mozzes.remoting.common.RemotingAction;
import org.mozzes.remoting.common.RemotingActionExecutor;
import org.mozzes.remoting.common.RemotingException;
import org.mozzes.remoting.common.RemotingProtocol;
import org.mozzes.remoting.common.RemotingResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Listener za jednog klijent-a. Prihvata Akcije od klijenta, pokusava da ih izvrsi i vraca mu odgovarajuci odgovor.
 * 
 * @author Perica Milosevic
 * @version v1.5
 */
class RemotingClientListener extends Thread {
	private static final Logger logger = LoggerFactory.getLogger(RemotingClientListener.class);
	
    /** Socket preko kog se obavlja komunikacija sa RemotingClient-om */
    private Socket clientSocket = null;

	/** Communication protocol */
	private RemotingProtocol remotingProtocol = null;

    /** Da li radi ovaj Thread */
    private volatile boolean listening = true;

    private RemotingActionDispatcher dispatcher;

    private RemotingClientManager clientManager;

    /**
     * Pokretanje ClientListener-a
     */
    RemotingClientListener(RemotingActionDispatcher dispatcher, RemotingClientManager clientManager, Socket socket,
            String parentName, int number) {

        super(parentName + "-listener(" + number + ")-client(" + socket.getInetAddress().getHostAddress() + ")");

        this.dispatcher = dispatcher;
        this.clientManager = clientManager;
        this.clientSocket = socket;
    }

    @Override
    public void run() {
        try {
        	setClientInfo();
            initClientSocket();
        	
            // obradjuje klijenta
            doListening();
		} catch (IOException e) {
			logger.error("Exception during RemoteClientListener initialization", e);
			stopListening();
		} finally {
            // odjavljuje se kod managera nakon sto zavrsi sa radom
            clientManager.remove(this);
        }
    }

    /**
     * Zaustavljanje ClientListener-a
     */
    synchronized void stopListening() {
        if (!listening)
            return;

        listening = false;

        if (remotingProtocol != null) 
        	remotingProtocol.close();

        if (clientSocket != null) {
            try {
                clientSocket.close();
            } catch (IOException ignore) {
            }
            clientSocket = null;
        }
    }

    /**
     * Glavna petlja u kojoj Thread prihvata i obradjuje akcije sve dok ne bude zaustavljen
     */
    private void doListening() {
        while (listening) {
            Thread.yield();
            try {
                processAction();
            } catch (Throwable ex) {
            	final String msg = "Exception during action processing. Stopping listener";
				if (listening)
            		logger.info(msg, ex);
            	else 
            		logger.debug(msg, ex);
                stopListening();
            }
        }
    }

    /**
     * Cita akciju, procesira je i salje odgovor nazad
     * 
     * @throws IOException Ukoliko je doslo do greske prilikom citanja akcije ili slanja odgovora
     */
    private void processAction() throws IOException {
        if (!listening)
            return;

        try {
            // cekamo da primimo neku akciju od klijenta
        	logger.debug("processAction() before receiving action");
            RemotingAction action = (RemotingAction) remotingProtocol.receive();
            // Log.getInstance().addEntry(Log.LEVEL_INFO, getName() +
            // " je prihvatio zahtev za izvrsenjem akcije: " + action);

            // primili smo akciju, pokusavamo da pronadjemo odgovarajuceg
            // executor-a
            RemotingActionExecutor actionExecutor = dispatcher.getActionExecutor(action);

            // izvrsavanje akcije
            RemotingResponse response = null;
            try {
            	logger.debug("processAction() before executing action");
                response = actionExecutor.execute(action);
            } catch (RemotingException ex) {
                throw ex;
            } catch (Throwable thr) {
                throw new RemotingException(thr);
            }

            // akcija mora da vrati neki response klijentu
            if (response == null)
                response = new RemotingResponse(new HashMap<Object, Object>());

            logger.debug("processAction() before sending result");
            remotingProtocol.send(response);
            logger.debug("processAction() after sending result");
        } catch (ClassCastException ex) {
        	logger.error("Unknown class received from client", ex);
            remotingProtocol.send(new RemotingException(ex));
            logger.debug("processAction() after sending class cast exception");
        } catch (RemotingException ex) {
            // doslo je do greske prilikom izvrsavanja akcije
            // klijentu saljemo exception
        	logger.debug("processAction() before sending remoting exception", ex);
        	remotingProtocol.send(ex);
        	logger.debug("processAction() after sending remoting exception");
        }
    }

    private void initClientSocket() throws IOException {
    	clientSocket.setSoTimeout(0);
    	clientSocket.setKeepAlive(true);
    	remotingProtocol = RemotingProtocol.buildServerSide(clientSocket);
    }
    
	private void setClientInfo() {
		RemotingClientInfo.setIpAddress(clientSocket.getInetAddress().getHostAddress());
	}
    
}
