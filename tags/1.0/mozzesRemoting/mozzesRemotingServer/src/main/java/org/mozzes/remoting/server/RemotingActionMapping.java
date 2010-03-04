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

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.mozzes.remoting.common.RemotingActionExecutor;
import org.mozzes.remoting.common.RemotingActionExecutorProvider;
import org.mozzes.remoting.common.RemotingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Maper koji na osnovu naziva RemotingAction-a pronalazi fabriku koja proizvodi izvrsioca akcije (
 * {@link RemotingActionExecutor}-a). Implemnentacija je thread-safe
 * 
 * @author Perica Milosevic
 * @version 1.8.2
 */
public class RemotingActionMapping {

    private final Logger logger = LoggerFactory.getLogger(RemotingActionMapping.class);

    /**
     * Maper iz imena remote akcije u fabriku njenog executor-a
     */
    private ConcurrentMap<String, RemotingActionExecutorProvider> nameActionMapper;

    /**
     * Kreiranje praznog mapiranja, bez definisanih akcija
     */
    public RemotingActionMapping() {
        this.nameActionMapper = new ConcurrentHashMap<String, RemotingActionExecutorProvider>();
    }

    /**
     * Kreiranje mapiranja na osnovu Properties-a u kome su kljucevi nazivi akcija, a vrednosti su nazivi klasa
     * RemotingActionExecutor-a ili nazivi klasa RCAExecutorFactory-ja. Ukoliko je u Properties-u naveden
     * RemotingActionExecutor onda se izvrsioci akcija kreiraju pozivanjem default konstruktora. Ukoliko je u
     * Properties-u naveden RCAExecutorFactory onda se izvrsioci akcija kreiraju pozivanjem navedenog
     * RCAExecutorFactory-ja. Dozvoljeno je da se u istom Properties-u za razlicite akcije navode i
     * RemotingActionExecutor-i i RCAExecutorFactory-ji
     * 
     * @param mappingProperties Properties sa definisanim mapiranjem
     * @throws RemotingException ukoliko mapiranje nije ispravno definisano
     */
    public RemotingActionMapping(Properties mappingProperties) throws RemotingException {
        this();
        addMapping(mappingProperties);
    }

    /**
     * Kreiranje mapiranja koje ima mapiranu samo jednu akciju
     * 
     * @param actionName Naziv mapirane akcije
     * @param mappedClassName Naziv klase izvrsioca ili fabrike izvrsioca
     * @throws RemotingException Ukoliko mapiranje nije ispravno
     */
    public RemotingActionMapping(String actionName, String mappedClassName) throws RemotingException {
        this();
        addMapping(actionName, mappedClassName);
    }

    /**
     * Kreiranje mapiranja koje ima mapiranu samo jednu akciju
     * 
     * @param actionName Naziv mapirane akcije
     * @param executorProvider Fabrika za proizvodnju izvrsilaca
     */
    public RemotingActionMapping(String actionName, RemotingActionExecutorProvider executorProvider) {
        this();
        addMapping(actionName, executorProvider);
    }

    /**
     * Pronalazi i vraca fabriku za izvrsioca remote akcije
     * 
     * @param actionName - naziv akcije
     * @return RCAExecutorFactory - fabrika izvrsilaca akcije
     */
    final RemotingActionExecutorProvider getExecutorProvider(String actionName) {
        return nameActionMapper.get(actionName);
    }

    /**
     * Dodaje sva mapiranja definisana u drugom RemotingActionMapping-u
     * 
     * @param other RemotingActionMapping cija se mapiranja dodaju
     */
    public void addMapping(RemotingActionMapping other) {
        if (other == null)
            return;

        this.nameActionMapper.putAll(other.nameActionMapper);
    }

    /**
     * Dodaje novo mapiranje
     * 
     * @param actionName Naziv mapirane akcije
     * @param mappedClassName Naziv klase izvrsioca ili fabrike izvrsioca
     * @throws RemotingException Ukoliko mapiranje nije ispravno
     */
    public void addMapping(String actionName, String mappedClassName) throws RemotingException {
        addMapping(actionName, createMappedExecutorFactory(mappedClassName));
    }

    /**
     * Dodaje novo mapiranje
     * 
     * @param actionName naziv akcije
     * @param executorProvider fabrika koja zna da kreira executor-a
     */
    public void addMapping(String actionName, RemotingActionExecutorProvider executorProvider) {
        nameActionMapper.put(actionName, executorProvider);
    }

    /**
     * Dodaje nove akcije u mapiranje na osnovu Properties-a u kome su kljucevi nazivi akcija, a vrednosti su nazivi
     * klasa RemotingActionExecutor-a ili nazivi klasa RCAExecutorFactory-ja. Ukoliko je u Properties-u naveden
     * RemotingActionExecutor onda se izvrsioci akcija kreiraju pozivanjem default konstruktora. Ukoliko je u
     * Properties-u naveden RCAExecutorFactory onda se izvrsioci akcija kreiraju pozivanjem navedenog
     * RCAExecutorFactory-ja. Dozvoljeno je da se u istom Properties-u za razlicite akcije navode i
     * RemotingActionExecutor-i i RCAExecutorFactory-ji
     * 
     * @param mappingProperties Properties sa definisanim mapiranjem
     * @throws RemotingException ukoliko mapiranje nije ispravno definisano
     */
    public void addMapping(Properties mappingProperties) throws RemotingException {
        if (mappingProperties != null) {
            for (Iterator<Object> it = mappingProperties.keySet().iterator(); it.hasNext();) {
                String actionName = (String) it.next();
                String mappedClassName = mappingProperties.getProperty(actionName);
                addMapping(actionName, mappedClassName);
            }
        }
    }

    /**
     * Na osnovu naziva klase kreira RCAExecutorFactory. 1) Ako klasa predstavlja RemotingActionExecutor-a onda se
     * kreira DefaultRCAExecutorFactory koji obavlja kreiranje RemotingActionExecutor-a pozivanjem default konstruktora.
     * 2) Ako klasa predstavlja RCAExecutorFactory onda se taj factory biti korisce prilikom kreiranja executora
     * 
     * @param mappedClassName Naziv klase
     * @return RCAExecutorFactory koji ce se koristiti za kreiranje executora
     * 
     * @throws RemotingException Ukoliko iz naziva klase ne moze da se obavi kreiranje RCAExecutorFactory-ja
     */
    @SuppressWarnings("unchecked")
    private RemotingActionExecutorProvider createMappedExecutorFactory(String mappedClassName) throws RemotingException {
        if (mappedClassName == null)
            throw new RemotingException("Invalid RemotingAction mapping, executor is null");

        Class<?> mappedClass = null;
        try {
            mappedClass = Class.forName(mappedClassName);
        } catch (ClassNotFoundException e) {
            throw new RemotingException("Invalid RemotingAction mapping, executor: " + mappedClassName);
        }

        // proveravamo da li je mapiranje vezano direktno za executora
        if (RemotingActionExecutor.class.isAssignableFrom(mappedClass))
            return new DefaultRemotingActionExecutorProvider((Class<? extends RemotingActionExecutor>) mappedClass);

        // proveravamo da li je mapiranje vezano za fabriku executora
        if (RemotingActionExecutorProvider.class.isAssignableFrom(mappedClass)) {
            try {
                return (RemotingActionExecutorProvider) mappedClass.newInstance();
            } catch (InstantiationException e) {
                throw new RemotingException(e);
            } catch (IllegalAccessException e) {
                throw new RemotingException(e);
            }
        }

        throw new RemotingException("Invalid RemotingAction mapping, executor: " + mappedClassName);
    }

    /**
     * Default fabrika za RemotingActionExecutor-e. Prilikom konstrukcije prima klasu i kasnije poziva njen default
     * contructor.
     */
    private class DefaultRemotingActionExecutorProvider implements RemotingActionExecutorProvider {

        /**
         * Klasa RemotingActionExecutor-a
         */
        private Class<? extends RemotingActionExecutor> executorClass;

        /**
         * Kreiranje default fabrike.
         * 
         * @param executorClass Klasa RemotingActionExecutor-a
         * @throws RemotingException ukoliko ne postoji default konstruktor kod RemotingActionExecutor-a
         */
        private DefaultRemotingActionExecutorProvider(Class<? extends RemotingActionExecutor> executorClass)
                throws RemotingException {
            setExecutorClass(executorClass);
        }

        public RemotingActionExecutor get() {
            try {
                return executorClass.newInstance();
            } catch (InstantiationException e) {
                logger.error("Unable to create executor", e);
            } catch (IllegalAccessException e) {
                logger.error("Unable to create executor", e);
            }
            return null;
        }

        private void setExecutorClass(Class<? extends RemotingActionExecutor> executorClass) throws RemotingException {
            if (!hasDefaultContructor(executorClass))
                throw new RemotingException("Default constructor required for RemotingActionExecutor: "
                        + executorClass.getName());

            this.executorClass = executorClass;
        }

        /**
         * Provera da li klasa ima default konstruktor (bez parametara)
         * 
         * @param clazz - klasa koja se proverava
         * @return true - ukoliko klasa ima default konstruktor
         */
        private boolean hasDefaultContructor(Class<? extends RemotingActionExecutor> clazz) {
            try {
                return (clazz.getConstructor(new Class[] {})) != null;
            } catch (SecurityException ignore) {
            } catch (NoSuchMethodException ignore) {
            }
            return false;
        }
    }
}
