package org.mozzes.swing.component.convinient;

import java.util.Arrays;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.mozzes.swing.utils.ToggleListSelectionModel;

import net.miginfocom.swing.MigLayout;



/**
 * panel na kojem je smestena lista, sa pripadajucim labelama
 * IDEJA: da korisnik moze vizuelno da uoci razliku izmedju
 * liste koja ima singleselectionmode i liste koja ima multipleselectionmode
 * KAKO: pored standardne labele koja oznacava sta se u listi nalazi bice i
 * dodatna labela u dnu koja ce prikazivati broj izabranih elemeata iz liste
 * npr: Br. izabranih drzava: 3
 * 
 * @author nikolad
 *
 */
public class MultipleSelectionListPanel extends JPanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7457183949150370837L;
	private JLabel titleLabel;
	private JLabel statusLabel;
	private String title;
	private String statusText;
	private JList list;
	
	/**
	 *<p>
	 * @param title - tekst labele koja stoji iznad liste[proslediti prevod a ne kljuc]
	 * @param statusText - text koji ce da se nalazi ispod liste, koji ce da sugerise
	 * mogucnost visestrukog selektovanja npr "Br izabranih komp. : " [proslediti prevod a ne kljuc] </p>
	 * nije hendlovano prosledjivanje null vrednosti!
	 */
	public MultipleSelectionListPanel(String title, String statusText, List<Object> elements){
		super();
		this.title = title;
		this.statusText = statusText;
		init();
		addElementz(elements);
		
	}
	/**
	 *<p>
	 * @param title - tekst labele koja stoji iznad liste[proslediti prevod a ne kljuc]
	 * @param statusText - text koji ce da se nalazi ispod liste, koji ce da sugerise
	 * mogucnost visestrukog selektovanja npr "Br izabranih komp. : " [proslediti prevod a ne kljuc] </p>
	 * nije hendlovano prosledjivanje null vrednosti!
	 */
	public MultipleSelectionListPanel(String title, String statusText){
		super();
		this.title = title;
		this.statusText = statusText.trim();
		init();
	}
	
	
	/**
	 * bagerisanje
	 */
	private void init(){
		createComponents();
		doLayoutStuff();
		list.addListSelectionListener(new ListSelectionListener() {
			
			@Override
			public void valueChanged(ListSelectionEvent e) {
				refreshStatusMessage();
			}
		});
		refreshStatusMessage();
	}


	/**
	 * kreiranje komponenti
	 */
	private void createComponents() {
		titleLabel = new JLabel(title); 
		statusLabel = new JLabel(statusText);
		list = new JList();
		list.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		list.setSelectionModel(new ToggleListSelectionModel());
	}
	
	/**
	 * zmajski mig layout
	 */
	private void doLayoutStuff(){
		setLayout(new MigLayout("fill"));
		add(titleLabel, "wrap 3");
		JScrollPane scrollPane = new JScrollPane(list); 
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		add(scrollPane, "grow,h 100:100:, w 0:0:, wrap 3");
		add(statusLabel);
	}
	
	
	/**
	 * kreiramo model i dodeljujemo ga listi
	 */
	private void addElementz(List<?> elements){
		if(elements == null)
			return;
		DefaultListModel model = new DefaultListModel();
		for (Object item : elements) {
			model.addElement(item);
		}
		list.setModel(model);
	}
	
	
	/**
	 * @param elements Elementi koji ce biti prikazani u listi
	 */
	public void setElemenents(List<?> elements){
		addElementz(elements);
	}
	
	/**
	 * vodi racuna o prikazu broja izabranih 
	 * elemenata iz liste
	 */
	private void refreshStatusMessage(){
		StringBuffer sb = new StringBuffer();
		sb.append(statusText);
		sb.append(" ");
		sb.append(getSelectedValues().length);
		statusLabel.setText(sb.toString());
	}
	
	
	/**
	 * @return Vraca izabrane vrednosti iz liste
	 */
	public Object[] getSelectedValues(){
		return list.getSelectedValues();
	}
	
	
	/**
	 * utility metoda koja vraca izabrane vrednsti iz liste[komponente] kao listu 
	 */
	
	public List<?> getSelectedValuesAsList(){
		return Arrays.asList(list.getSelectedValues());
	}
	
	/**
	 * Lista je dostupna svima
	 * @return Lista
	 */
	public JList getList(){
		return list;
	}
	
	public void setStatusLabelVisible(boolean visible) {
		statusLabel.setVisible(visible);
	}
	
}
