package org.mozzes.event.service;

/**
 * Registar u kome se nalaze svi servisi u sistemu.
 * <p>
 * Svaki servis koji postoji u sistemu se mora prijaviti registru tako sto ce se prvo registrovati pozivanjem metode
 * {@link #register(String, ServiceConfiguration)}. Tek nakon uspesne registracije se mogu dohvatati implementacije
 * servisa metodom {@link #get(String)}. Ukoliko servis vise nije potreban, dovoljno je pozvati metodu
 * {@link #unregister(String)} i servisa nece biti u registru. Implementacija servis registra mora da garantuje da nece
 * biti moguce dohvatiti servis koji nije registrovan ili koji je odregistrovan. Obrnuto ne vazi, tj. ne garantuje se
 * uspesno dohvatanje servisa ukoliko je on registrovan u registriju.
 * <p>
 * Ime servisa je i njegov identifikator. Preko imena servisa se on deregistruje i samo preko imena se moze dobiti
 * implementacija servisa. Ime servisa u svim metodama gde se koristi mora biti validno. Validno ime je bilo koji
 * {@link String} koji nije <code>null</code> i koji nije prazan.
 * 
 * @author Perica Milosevic
 * @version 1.8.2
 */
public interface ServiceRegistry {

	/**
	 * Registruje novi servis u registru. Ime servisa je jedinstveni identifikator servisa po kome ce se kasnije on
	 * dohvatati. Nemoguce je registrovati dva servisa sa istim imenom.. Kada se novi servis uspesno registruje, moguce
	 * je koristiti metodu {@link #get(String)} za dohvatanje implementacije servisa.
	 * 
	 * @param serviceName Naziv servisa koji se registruje koji ce sluziti i kao njegov identifikator. Ne moze biti
	 *            <code>null</code>.
	 * @param serviceConfiguration Konfiguracija servisa. Mora biti validno ime servisa
	 * @throws ServiceException Ukoliko nije uspela registracija. Jedni od razloga zasto registracija ne moze uspeti je
	 *             ukoliko je neki od argumenata <code>null</code>, ime servisa nije validno ili ako je servis pod
	 *             tim imenom vec registrovan
	 * @see #unregister(String)
	 */
	public void register(String serviceName, ServiceConfiguration serviceConfiguration) throws ServiceException;

	/**
	 * Ukida registraciju servisa. Ime servisa je jedinstveni identifikator servisa koji treba odregistrovati. Posle
	 * izvrsene uspesno odregistracije, ne moze se vise dohvatati implementacija servisa metodom {@link #get(String)}.
	 * 
	 * @param serviceName Naziv servisa koji se ukida. Mora biti validno ime servisa
	 * @throws ServiceException Ukoliko nije uspelo ukidanje servisa. Moguci razlozi za neuspeh je da ime servisa nije
	 *             validno ili da servis sa tim imenom nije ni registrovan pre poziva ove metode.
	 * @see #register(String, ServiceConfiguration)
	 */
	public void unregister(String serviceName) throws ServiceException;

	/**
	 * Vraca implementaciju registrovanog servisa. Ime servisa je jedinstveni identifikator servisa ciju implementaciju
	 * treba dohvatiti.
	 * 
	 * @param serviceName Naziv trazenog servisa. Mora biti validno ime servisa.
	 * @return Implementacija registrovanog servisa
	 * @throws ServiceException Ukoliko servis ne postoji ili ime servisa nije validno
	 */
	public Object get(String serviceName) throws ServiceException;

}