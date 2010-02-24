package org.mozzes.swing.component;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Scrollbar;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.RowSorter;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.TableModelEvent;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import org.mozzes.swing.component.jtablegh.GroupableTableHeader;
import org.mozzes.swing.component.jtablegh.JTableGH;
import org.mozzes.swing.component.table.OrdinalNumberModel;
import org.mozzes.swing.mgf.component.table.rendering.CellRenderingFacility;


/**
 * {@link JPanel} objekat koji sadrzi swing kontrole za prikaz podataka u tabelarnom formatu unutar kojeg se odredjeni 
 * broj kolona ne "skroluje" po horizontali. Kolone koje se ne skroluju po horizontali pozicionirane su sa leve strane i 
 * odvojene su {@link JSplitPane} objektom od kolona koje se skroluju i koje su pozicionirane sa desne strane.
 * @author Borko Grecic
 * 
 * @param <T> Genericki tip koji je pod-tip {@link TableModel} objekta
 */
public class FixedColumnTable <T extends TableModel> extends JPanel {
	
	/**
	 * Serial version UID
	 */
	private static final long serialVersionUID = 1893141833166263781L;
	
	/**
	 * {@link Map} objekat unutar koga se nalaze svi {@link TableCellRenderer} objekti za odredjene kolone
	 */
	private final Map<Integer, TableCellRenderer> addedRenderers = new HashMap<Integer, TableCellRenderer>();
	
	/**
	 * {@link Map} objekat unutar koga se nalaze svi {@link TableCellEditor} objekti za odredjene kolone
	 */
	private final Map<Integer, TableCellEditor> addedEditors = new HashMap<Integer, TableCellEditor>();
	
	/**
	 * {@link Map} objekat unutar koga se nalaze zeljene sirine za odredjene kolone
	 */
	private final Map<Integer, Integer> addedColumnWidths = new HashMap<Integer, Integer>();
	
	/**
	 * "Prefered" sirina kolona tabele koja se skroluje po horizontali. Ovaj property se uzima u obzir
	 * samo ako nije null i vazi za sve kolone.
	 */
	private Integer preferedScrollableColumnWidth;

	/**
	 * Broj kolona koje je potrebno prikazati u tabeli koja se ne skroluje (u levoj tabeli)
	 */
	private int fixedColumns;
	
	/**
	 * {@link JTable} objekat koji prikazuje kolone koje se skroluju po horizontali
	 */
	private JTable scrollableTable;
	
	/**
	 * {@link JTable} objekat koji prikazuje kolone koje se ne skroluju po horizontali
	 */
	private JTable fixedTable;
	
	/**
	 * {@link JScrollPane} objekat za tabelu koja se ne skroluje po horizontali
	 */
	private JScrollPane fixedTableScrollPane;
	
	/**
	 * {@link JScrollPane} objekat za tabelu koja se skroluje po horizontali
	 */
	private JScrollPane scrollableTableScrollPane;
	
	/**
	 * {@link AbstractTableModel} objekat koji formira podatke o broju redova
	 */
	private AbstractTableModel ordinalNumberTableModel;
	
	/**
	 * {@link JSplitPane} objekat koji sadrzi sve komponente {@link FixedColumnTable} objekta
	 */
	private JSplitPane splitPane;
	
	private CellRenderingFacility cellRenderingFacilityFixed ;
	
	private CellRenderingFacility cellRenderingFacilityScrollable;
	
	/**
	 * Konstruise instancu {@link FixedColumnTable} objekta cije tabele imaju standardne {@link JTableHeader} objekte
	 * @param <P> Tip {@link TableModel} objekta
	 * @param fixedColumns Broj fiksnih kolona
	 * @param tableModel Instanca {@link TableModel} objekta
	 * @return {@link FixedColumnTable}
	 */
	public static <P extends TableModel> FixedColumnTable<P> buildStandardHeaderInstance(final int fixedColumns, final P tableModel) {
		final FixedColumnTable<P> fixedColumnTable = new FixedColumnTable<P>();
		final TableRowSorter<P> tableRowSorter = new TableRowSorter<P>(tableModel);
		fixedColumnTable.validateParameters(fixedColumns, tableModel, tableRowSorter);
		fixedColumnTable.scrollableTable = fixedColumnTable.createScrollableTable(tableModel, tableRowSorter);
		fixedColumnTable.fixedTable = fixedColumnTable.createFixedTable(tableModel, tableRowSorter);
		fixedColumnTable.buildInstance(fixedColumns, tableModel, tableRowSorter);
		return fixedColumnTable;
	}
	
	/**
	 * Konstruise instancu {@link FixedColumnTable} objekta cije tabele imaju grupabilne {@link JTableHeader} objekte
	 * @param <R> Tip {@link TableModel} objekta
	 * @param fixedColumns Broj fiksnih kolona
	 * @param tableModel Instanca {@link TableModel} objekta
	 * @return {@link FixedColumnTable}
	 */
	public static <R extends TableModel> FixedColumnTable<R> buildGroupableHeaderInstance(final int fixedColumns, final R tableModel) {
		final FixedColumnTable<R> fixedColumnTable = new FixedColumnTable<R>();
		final TableRowSorter<R> tableRowSorter = new TableRowSorter<R>(tableModel);
		fixedColumnTable.validateParameters(fixedColumns, tableModel, tableRowSorter);
		fixedColumnTable.scrollableTable = fixedColumnTable.createGroupHeaderScrollableTable(tableModel, tableRowSorter);
		fixedColumnTable.fixedTable = fixedColumnTable.createGroupHeaderFixedTable(tableModel, tableRowSorter);
		fixedColumnTable.buildInstance(fixedColumns, tableModel, tableRowSorter);
		fixedColumnTable.synchronizeTableHeaderRefreshAndScrollBar();
		return fixedColumnTable;
	}
	
	/**
	 * Konstruise instancu {@link FixedColumnTable} objekta cije tabele imaju grupabilne {@link JTableHeader} objekte
	 * @param <R> Tip {@link TableModel} objekta
	 * @param fixedColumns Broj fiksnih kolona
	 * @param tableModel Instanca {@link TableModel} objekta
	 * @param tableRowSorter {@link RowSorter} objekat za sortiranje tabele
	 * @return {@link FixedColumnTable}
	 */
	public static <R extends TableModel> FixedColumnTable<R> buildGroupableHeaderInstance(final int fixedColumns, final R tableModel, final RowSorter<R> tableRowSorter) {
		final FixedColumnTable<R> fixedColumnTable = new FixedColumnTable<R>();
		fixedColumnTable.validateParameters(fixedColumns, tableModel, tableRowSorter);
		fixedColumnTable.scrollableTable = fixedColumnTable.createGroupHeaderScrollableTable(tableModel, tableRowSorter);
		fixedColumnTable.fixedTable = fixedColumnTable.createGroupHeaderFixedTable(tableModel, tableRowSorter);
		fixedColumnTable.buildInstance(fixedColumns, tableModel, tableRowSorter);
		fixedColumnTable.synchronizeTableHeaderRefreshAndScrollBar();
		return fixedColumnTable;
	}

	/**
	 * Konstruktor {@link FixedColumnTable} objekta.
	 */
	private FixedColumnTable() {
		super(new GridBagLayout());
	}
	
	/**
	 * Sadrzi svu neophodnu implementaciju za konstruisanje i inicijalizaciju svih swing komponenti.
	 * @param nuberOfFixedColumns Broj kolona sa leve strane koje se ne skroluju po horizontali
	 * @param tableModel {@link TableModel} objekat koji sadrzi podatke za prikaz
	 * @param rowSorter {@link RowSorter} objekat zaduzen za sortiranje podatakar
	 */
	private void buildInstance(final int nuberOfFixedColumns, final T tableModel, final RowSorter<T> rowSorter) {
		this.fixedColumns = nuberOfFixedColumns;
		this.fixedTableScrollPane = createFixedTableScrollPane();
		this.scrollableTableScrollPane = createScrollableTableScrollPane();
		
		addOrdinalNumberTable(tableModel);
		synchronizeHorizontalScrollBar();
		synchronizeVerticalScrollBar();
		splitTableColumns();
		createSplitPane();
	}
	
	/**
	 * Dodaje tabelu koja se prikazuje na krajnjem levom delu komponente i koja prikazuje redni broj svakog reda
	 * @param tableModel {@link TableModel} objekat na osnovu koga se formiraju redni brojevi
	 */
	private void addOrdinalNumberTable(final T tableModel) {
		this.ordinalNumberTableModel = new OrdinalNumberModel<T>(tableModel);
		JTable table = new JTable(this.ordinalNumberTableModel);
		table.getColumnModel().getColumn(0).setPreferredWidth(45);
		table.setPreferredScrollableViewportSize(table.getPreferredSize());
		table.setSelectionModel(this.fixedTable.getSelectionModel());
		table.getTableHeader().setReorderingAllowed(false);
		this.fixedTableScrollPane.setRowHeaderView(table);
		this.fixedTableScrollPane.setCorner(ScrollPaneConstants.UPPER_LEFT_CORNER, table.getTableHeader());
	}

	/**
	 * Proverava ispravnost prosledjenih parametra.
	 * @param numberOfFixedColumns Broj kolona koje se ne skroluju
	 * @param tableModel {@link TableModel} objekat koji sadrzi podatke za prikaz
	 * @param rowSorter {@link RowSorter} objekat koji sadrzi logiku za sortiranje podataka u tabeli
	 */
	private void validateParameters(int numberOfFixedColumns, T tableModel, RowSorter<T> rowSorter) {
		if(numberOfFixedColumns < 0)
			throw new IllegalArgumentException("Broj kolona koje se ne skroluju ne sme biti manji od nule (0)");
		if(tableModel == null)
			throw new IllegalArgumentException("Prosledjeni TableModel objekat ne sme biti null");
		if(rowSorter == null) 
			throw new IllegalArgumentException("Prosledjeni RowSorter objekat ne sme biti null");
	}

	
	/**
	 * Konstruise {@link JTable} objekat koji prikazuje kolone koje se skroluju po horizontali.
	 * @param tableModel {@link TableModel} objekat koji sadrzi podatke za prikaz
	 * @param rowSorter {@link RowSorter} objekat koji implementira logiku za sortiranje podataka unutar tabele
	 * @return {@link JTable}
	 */
	private JTable createScrollableTable(final T tableModel, final RowSorter<T> rowSorter) {
		this.scrollableTable = new SplitTable(tableModel);
		this.scrollableTable.setRowSorter(rowSorter);
		this.scrollableTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		this.scrollableTable.getTableHeader().setReorderingAllowed(false);
		cellRenderingFacilityScrollable = new CellRenderingFacility(scrollableTable);
		return this.scrollableTable;
	}
	
	/**
	 * Konstruise {@link JTable} objekat sa grupabilnim header-om koji prikazuje kolone koje se skroluju po horizontali.
	 * @param tableModel {@link TableModel} objekat koji sadrzi podatke za prikaz
	 * @param rowSorter {@link RowSorter} objekat koji implementira logiku za sortiranje podataka unutar tabele
	 * @return {@link JTable}
	 */
	private JTable createGroupHeaderScrollableTable(final T tableModel, final RowSorter<T> rowSorter) {
		this.scrollableTable = new GroupHeaderSplitTable(tableModel);
		this.scrollableTable.setRowSorter(rowSorter);
		this.scrollableTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		this.scrollableTable.getTableHeader().setReorderingAllowed(false);
		this.scrollableTable.getTableHeader().setPreferredSize(new Dimension(this.scrollableTable.getTableHeader().getPreferredSize().width, 36));
		return this.scrollableTable;
	}
	

	
	/**
	 * Konstruise {@link JTable} objekat koji prikazuje kolone koje se ne skroluju po horizontali.
	 * @param tableModel {@link TableModel} objekat koji sadrzi podatke za prikaz
	 * @param rowSorter {@link RowSorter} objekat koji implementira logiku za sortiranje podataka unutar tabele
	 * @return {@link JTable}
	 */
	private JTable createFixedTable(final T tableModel, final RowSorter<T> rowSorter) {
		this.fixedTable = new SplitTable();
		this.fixedTable.setAutoCreateColumnsFromModel(false);
		this.fixedTable.setModel(tableModel);
		this.fixedTable.setRowSorter(rowSorter);
		this.fixedTable.setSelectionModel(this.scrollableTable.getSelectionModel());
		this.fixedTable.getTableHeader().setReorderingAllowed(false);
		cellRenderingFacilityFixed = new CellRenderingFacility(fixedTable);
		return this.fixedTable;
	}
	
	/**
	 * Konstruise {@link JTable} objekat sa grupabilnim header-om koji prikazuje kolone koje se ne skroluju po horizontali.
	 * @param tableModel {@link TableModel} objekat koji sadrzi podatke za prikaz
	 * @param rowSorter {@link RowSorter} objekat koji implementira logiku za sortiranje podataka unutar tabele
	 * @return {@link JTable}
	 */
	private JTable createGroupHeaderFixedTable(final T tableModel, final RowSorter<T> rowSorter) {
		this.fixedTable = new GroupHeaderSplitTable();
		this.fixedTable.setAutoCreateColumnsFromModel(false);
		this.fixedTable.setModel(tableModel);
		this.fixedTable.setRowSorter(rowSorter);
		this.fixedTable.setSelectionModel(this.scrollableTable.getSelectionModel());
		this.fixedTable.getTableHeader().setReorderingAllowed(false);
		this.fixedTable.getTableHeader().setPreferredSize(new Dimension(this.fixedTable.getTableHeader().getPreferredSize().width, 36));
		return this.fixedTable;
	}
	
	/**
	 * Kreira {@link JScrollPane} objekat koji prikazuje tabelu koja sadrzi kolone koje se ne skroluju po horizontali
	 * @return {@link JScrollPane}
	 */
	private JScrollPane createFixedTableScrollPane() {
		final JScrollPane fixedScrollPane = new JScrollPane(this.fixedTable);
		fixedScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
		return fixedScrollPane;
	}
	
	/**
	 * Kreira {@link JScrollPane} objekat koji prikazuje tabelu koja sadrzi kolone koje se skroluju po horizontali
	 * @return {@link JTable}
	 */
	private JScrollPane createScrollableTableScrollPane() {
		return new JScrollPane(this.scrollableTable);
	}
	
	/**
	 * Sadrzi logiku koja sinhronizuje pojavljivanje horizontalnih {@link JScrollBar} objekata.
	 */
	private void synchronizeHorizontalScrollBar() {
		this.scrollableTableScrollPane.getHorizontalScrollBar().addComponentListener(new ComponentAdapter() {
			@Override
			public void componentHidden(ComponentEvent e) {
				FixedColumnTable.this.fixedTableScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
			}
			@Override
			public void componentShown(ComponentEvent e) {
				FixedColumnTable.this.fixedTableScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);	
			}
		});
	}

	/**
	 * Sadrzi logiku koja sinhronizuje poziciju vertikalnog {@link Scrollbar} objekta
	 */
	private void synchronizeVerticalScrollBar() {
		this.fixedTableScrollPane.getVerticalScrollBar().setModel(this.scrollableTableScrollPane.getVerticalScrollBar().getModel());
	}
	
	/**
	 * Deli kolone iz prosledjenog {@link JTable} objekta na kolone koje se skroluju i kolone koje se ne skroluju po horizontali
	 */
	private void splitTableColumns() {
		if(areBothTablesValid()) {
			removeColumnsFromFixedTable();
			passColumnsFromScrollableToFixedTable();
			insertCellRenderers();
			insertCellEditors();
			resizeScrollableColumnWidth();
			insertPreferedColumnWidths();
			refrechOrdinalData();
		}
	}

	/**
	 * Da li su obe tabele (tabela koja prikazuje kolone koje se ne skroluju po horizontali i 
	 * tabela koja prikazuje kolone koje se skroluju po horizontali) ispravno inicijalizovane
	 */
	private boolean areBothTablesValid() {
		return (this.scrollableTable != null && this.fixedTable != null);
	}
	
	/**
	 * Ako postoje, uklanja sve kolone iz tabele koja prikazuje kolone koje se ne skroluju po horizontali
	 */
	private void removeColumnsFromFixedTable() {
		while (this.fixedTable.getColumnCount() > 0) {
			this.fixedTable.getColumnModel().removeColumn(this.fixedTable.getColumnModel().getColumn(0));
		}
	}
	
	/**
	 * Prebacuje zadati broj kolona iz tabele koja prikazuje kolone koje se skroluju po horizontali u 
	 * tabelu koja prikazuje kolone koje se ne skroluju po horizontali
	 */
	private void passColumnsFromScrollableToFixedTable() {
		while(fixedTableNeedsMoreColumns() && scrollableTableHasMoreColumns()) {
			TableColumnModel columnModel = this.scrollableTable.getColumnModel();
	        TableColumn column = columnModel.getColumn(0);
    	    columnModel.removeColumn(column);
			this.fixedTable.getColumnModel().addColumn(column);
		}
	}
	
	/**
	 * Utvrdjuje da li je tabeli koja prikazuje sadrzaj koji se ne skroluje potrebno dodati jos kolona
	 */
	private boolean fixedTableNeedsMoreColumns() {
		return this.fixedTable.getColumnModel().getColumnCount() < this.fixedColumns;
	}

	/**
	 * Utvrdjuje da li je broj kolona tabele koja prikazuje sadrzaj koji se skroluje veci od nula (0)
	 */
	private boolean scrollableTableHasMoreColumns() {
		return (this.scrollableTable.getColumnModel().getColumnCount() > 0);
	}
	
	/**
	 * Re-inicijalizuje (ponovo postavla) {@link TableCellRenderer} objekte
	 * (Ovom akcijom se obezbedjuje postavljanje odgovarajucih {@link TableCellRenderer}-a novo-dodatim kolonama)
	 */
	private void insertCellRenderers() {
		for(Integer columnIndex : this.addedRenderers.keySet())
			setCellRenderer(columnIndex.intValue(), this.addedRenderers.get(columnIndex));
	}

	/**
	 * Re-inicijalizuje (ponovo postavla) {@link TableCellEditor} objekte
	 * (Ovom akcijom se obezbedjuje postavljanje odgovarajucih {@link TableCellEditor}-a novo-dodatim kolonama)
	 */
	private void insertCellEditors() {
		for(Integer columnIndex : this.addedEditors.keySet())
			setCellEditor(columnIndex.intValue(), this.addedEditors.get(columnIndex));
	}

	/**
	 * Re-inicijalizuje (ponovo postavla) sirinu kolona
	 * (Ovom akcijom se obezbedjuje postavljanje odgovarajucih sirina novo-dodatim kolonama)
	 */
	private void insertPreferedColumnWidths() {
		for(Integer columnIndex : this.addedColumnWidths.keySet())
			setPreferredColumnWidth(columnIndex.intValue(), this.addedColumnWidths.get(columnIndex).intValue());
	}
	
	/**
	 * Kreira horizontalni {@link JSplitPane} objekat koji sa leve strane sadrzi tabelu koja sadrzi kolone koje se ne 
	 * skroluju po horizontali, dok sa desne strane sadrzi tabelu koja sadrzi kolone koje se skroluju po horizontali.
	 */
	private void createSplitPane() {
		this.splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, this.fixedTableScrollPane, this.scrollableTableScrollPane);
		this.add(this.splitPane, 
				new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
	}
	
	/**
	 * Sinhronizuje osvezavanje {@link JTableHeader}-a sa skrolovanjem 
	 */
	private void synchronizeTableHeaderRefreshAndScrollBar() {
		this.scrollableTableScrollPane.getHorizontalScrollBar().addAdjustmentListener(new AdjustmentListener() {
			@Override
			public void adjustmentValueChanged(AdjustmentEvent e) {
				FixedColumnTable.this.scrollableTable.getTableHeader().setPreferredSize(new Dimension
						(FixedColumnTable.this.scrollableTable.getPreferredSize().width, FixedColumnTable.this.scrollableTable.getTableHeader().getPreferredSize().height));
			}
		});
	}
	
	/**
	 * Dodaje prosledjeni {@link TableCellRenderer} objekat koloni ciji se indeks prosledjuje kao parametar
	 * @param columnIndex Indeks kolone na kojoj je potrebno postaviti renderer
	 * @param cellRenderer {@link TableCellRenderer}
	 */
	public void addCellRenderer(final int columnIndex, final TableCellRenderer cellRenderer) {
		validateCellRenderer(columnIndex, cellRenderer);
		this.addedRenderers.put(Integer.valueOf(columnIndex), cellRenderer);
		setCellRenderer(columnIndex, cellRenderer);
	}

	/**
	 * Vrsi validaciju prosledjenih parametara
	 * @param columnIndex Index kolone
	 * @param cellRenderer {@link TableCellRenderer} objekat
	 */
	private void validateCellRenderer(final int columnIndex, final TableCellRenderer cellRenderer) {
		if(columnIndex < 0)
			throw new IllegalArgumentException("Indeks kolone ne sme biti manji od 0");
		if(cellRenderer == null)
			throw new IllegalArgumentException("TableCellRenderer ne sme biti null");
	}
	
	/**
	 * Postavlja prosledjeni {@link TableCellRenderer} objekat koloni ciji se indeks prosledjuje kao parametar
	 * @param columnIndex Indeks kolone na kojoj je potrebno postaviti renderer
	 * @param cellRenderer {@link TableCellRenderer}
	 */
	private void setCellRenderer(final int columnIndex, final TableCellRenderer cellRenderer) {
		if(fixedTableHasColumnAtIndex(columnIndex))
			this.fixedTable.getColumnModel().getColumn(columnIndex).setCellRenderer(cellRenderer);
		else if(scrollableTableHasColumnAtIndex(columnIndex))
			this.scrollableTable.getColumnModel().getColumn(columnIndex - this.fixedColumns).setCellRenderer(cellRenderer);
	}
	
	/**
	 * Dodaje prosledjeni {@link TableCellEditor} objekat koloni ciji se indeks prosledjuje kao parametar
	 * @param columnIndex Indeks kolone na kojoj je potrebno postaviti editor
	 * @param cellEditor {@link TableCellEditor}
	 */
	public void addCellEditor(final int columnIndex, final TableCellEditor cellEditor) {
		validateCellEditor(columnIndex, cellEditor);
		this.addedEditors.put(Integer.valueOf(columnIndex), cellEditor);
		setCellEditor(columnIndex, cellEditor);
	}
	
	/**
	 * Vrsi validaciju prosledjenih parametara
	 * @param columnIndex Index kolone
	 */
	private void validateCellEditor(final int columnIndex, final TableCellEditor cellEditor) {
		if(columnIndex < 0)
			throw new IllegalArgumentException("Indeks kolone ne sme biti manji od 0");
		if(cellEditor == null)
			throw new IllegalArgumentException("TableCellEditor ne sme biti null");
	}

	/**
	 * Postavlja prosledjeni {@link TableCellEditor} objekat koloni ciji se indeks prosledjuje kao parametar
	 * @param columnIndex Indeks kolone na kojoj je potrebno postaviti editor
	 * @param cellEditor {@link TableCellEditor}
	 */
	private void setCellEditor(final int columnIndex, final TableCellEditor cellEditor) {
		if(fixedTableHasColumnAtIndex(columnIndex))
			this.fixedTable.getColumnModel().getColumn(columnIndex).setCellEditor(cellEditor);
		else if(scrollableTableHasColumnAtIndex(columnIndex))
			this.scrollableTable.getColumnModel().getColumn(columnIndex - this.fixedColumns).setCellEditor(cellEditor);
	}
	
	/**
	 * Dodaje zeljenu sirinu koloni ciji se indeks prosledjuje kao parametar
	 * @param columnIndex Indeks kolone
	 * @param columnWidth Sirina kolone
	 */
	public void addPreferredColumnWidth(final int columnIndex, final int columnWidth) {
		if(columnIndex < 0 || columnWidth < 0)
			throw new IllegalArgumentException("Indeks kolone i/ili sirina kolone ne sme biti manja od 0");
		this.addedColumnWidths.put(Integer.valueOf(columnIndex), Integer.valueOf(columnWidth));
		setPreferredColumnWidth(columnIndex, columnWidth);
	}
	
	/**
	 * Postavlja zeljenu sirinu koloni ciji se indeks prosledjuje kao parametar
	 * @param columnIndex Indeks kolone
	 * @param columnWidth Sirina kolone
	 */
	private void setPreferredColumnWidth(final int columnIndex, final int columnWidth) {
		if(fixedTableHasColumnAtIndex(columnIndex))
			this.fixedTable.getColumnModel().getColumn(columnIndex).setPreferredWidth(columnWidth);
		else if(scrollableTableHasColumnAtIndex(columnIndex))
			this.scrollableTable.getColumnModel().getColumn(columnIndex - this.fixedColumns).setPreferredWidth(columnWidth);
	}
	
	/**
	 * Da li tabela koja prikazuje kolone koje se ne skroluju po horizontali 
	 * sadrzi kolonu ciji se indeks prosledjuje kao parametar
	 */
	private boolean fixedTableHasColumnAtIndex(final int columnIndex) {
		return ((this.fixedTable.getColumnCount() != 0) && (columnIndex < this.fixedColumns) && (columnIndex < this.fixedTable.getColumnCount()));
	}
	
	/**
	 * Da li tabela koja prikazuje kolone koje se skroluju po horizontali 
	 * sadrzi kolonu ciji se indeks prosledjuje kao parametar
	 */
	private boolean scrollableTableHasColumnAtIndex(final int columnIndex) {
		return ((this.scrollableTable.getColumnCount() != 0) && ((columnIndex - this.fixedColumns) < this.scrollableTable.getColumnCount()));
	}
	
	/**
	 * Postavlja prosledjeni {@link TableCellRenderer} objekat svim kolonama koje 
	 * prikazuju podatke koji su tipa prosledjenog {@link Class} objekta
	 * @param columnClass {@link Class} tip podataka
	 * @param renderer {@link TableCellRenderer}
	 */
	public void setDefaultRenderer(final Class<?> columnClass, final TableCellRenderer renderer) {
		validateDefaultRenderer(columnClass, renderer);		
		cellRenderingFacilityFixed.setClassRenderer(columnClass, renderer);
		cellRenderingFacilityScrollable.setClassRenderer(columnClass, renderer);
	}
	
	/**
	 * Vrsi validaciju prosledjenih parametra
	 * @param columnClass {@link Class}
	 * @param renderer {@link TableCellRenderer}
	 */
	private void validateDefaultRenderer(final Class<?> columnClass, final TableCellRenderer renderer) {
		if(columnClass == null)
			throw new IllegalArgumentException("Class objekat ne sme biti null");
		if(renderer == null)
			throw new IllegalArgumentException("TableCellRenderer objekat ne sme biti null");
	}

	/**
	 * Postavlja prosledjeni {@link TableCellEditor} objekat svim kolonama koje 
	 * prikazuju podatke koji su tipa prosledjenog {@link Class} objekta
	 * @param columnClass {@link Class} tip podataka
	 * @param editor {@link TableCellEditor}
	 */
	public void setDefaultEditor(final Class<?> columnClass, final TableCellEditor editor) {
		validateDefaultEditor(columnClass, editor);
		this.fixedTable.setDefaultEditor(columnClass, editor);
		this.scrollableTable.setDefaultEditor(columnClass, editor);
	}
	
	/**
	 * Vrsi validaciju prosledjenih parametra
	 */
	private void validateDefaultEditor(final Class<?> columnClass, final TableCellEditor editor) {
		if(columnClass == null)
			throw new IllegalArgumentException("Class objekat ne sme biti null");
		if(editor == null)
			throw new IllegalArgumentException("TableCellEditor objekat ne sme biti null");
	}

	/**
	 * Postavlja prosledjenu sirinu kolona svim kolonama tabele koja se prikazuje sa desne strane
	 * (tabele ciji se sadrzaj skroluje po horizontali)
	 */
	public void setPreferedScrollableColumnWidth(int preferedScrollableColumnWidth) {
		if(preferedScrollableColumnWidth < 0)
			throw new IllegalArgumentException("Sirina kolona ne sme biti manja od 0");
		this.preferedScrollableColumnWidth = Integer.valueOf(preferedScrollableColumnWidth);
		resizeScrollableColumnWidth();
	}
	
	/**
	 * Postavlja "default" sirinu kolona svim kolonama tabele koja se prikazuje sa desne strane
	 * (tabele ciji se sadrzaj skroluje po horizontali)
	 */
	private void resizeScrollableColumnWidth() {
		if(this.preferedScrollableColumnWidth != null && this.scrollableTable != null)
			for(int columnIndex = 0; columnIndex < this.scrollableTable.getColumnCount(); columnIndex++)
				this.scrollableTable.getColumnModel().getColumn(columnIndex).setPreferredWidth(this.preferedScrollableColumnWidth.intValue());
	}
	
	
	/**
	 * Osvezava podatke o rednim brojevima
	 */
	private void refrechOrdinalData() {
		if(this.ordinalNumberTableModel != null) {
			this.ordinalNumberTableModel.fireTableDataChanged();
			if(this.fixedTable.getColumnModel().getColumnCount() == 0)
				this.fixedTableScrollPane.getCorner(ScrollPaneConstants.UPPER_LEFT_CORNER).setVisible(false);
			else
				this.fixedTableScrollPane.getCorner(ScrollPaneConstants.UPPER_LEFT_CORNER).setVisible(true);
		}
	}
	
	/**
	 * Postavlja broj fiksnih kolona
	 * @param columnNumber Broj fiksnih kolona
	 */
	public void setFixedColumnsNumber(final int columnNumber) {
		this.fixedColumns = columnNumber;
	}
	
	/**
	 * Vraca broj fiksnih kolona
	 * @return Broj fiksnih kolona
	 */
	public int getFixedColumnsNumber() {
		return this.fixedColumns;
	}
	
	/**
	 * Vraca {@link JTable} objekat koji se scroll-uje po horizontali
	 * @return {@link JTable}
	 */
	public JTable getScrollableTable() {
		return this.scrollableTable;
	}
	
	/**
	 * Vraca {@link JSplitPane} objekat koji sadrzi sve elemente {@link FixedColumnTable} objekta
	 * @return {@link JSplitPane}
	 */
	public JSplitPane getJSplitPane() {
		return this.splitPane;
	}
	
	/**
	 * Vraca referencu na {@link JScrollPane} objekat koji sadrzi {@link JTable} objekat koji se skroluje po horizontali
	 * @return {@link JScrollPane}
	 */
	public JScrollPane getScrollableTableScrollPane() {
		return this.scrollableTableScrollPane;
	}
	
	/** 
	 * @return Vraca referencu na {@link JScrollPane} objekat koji sadrzi
	 */
	public JScrollPane getFixedTableScrollPane() {
		return this.fixedTableScrollPane;
	}
	
	/**
	 * Interni {@link JTable} objekat koji na svaki prosledjeni {@link TableModelEvent} 
	 * tipa TableModelEvent.HEADER_ROW objekat pokrece proces deljenja kolona
	 * @author Borko Grecic
	 */
	private class SplitTable extends JTable {

		/**
		 * Serial version UID 
		 */
		private static final long serialVersionUID = 6848355432633661793L;

		/**
		 * Konstruktor {@link SplitTable} objekta
		 */
		public SplitTable() {
			super();
		}

		/**
		 * Konstruktor {@link SplitTable} objekta
		 * @param tableModel {@link TableModel} objekat koji se prosledjuje {@link JTable} objektu
		 */
		public SplitTable(T tableModel) {
			super(tableModel);
		}

		/*
		 * (non-Javadoc)
		 * @see javax.swing.JTable#tableChanged(javax.swing.event.TableModelEvent)
		 */
		@Override
		public void tableChanged(TableModelEvent e) {
			super.tableChanged(e);
			if(e == null || (e.getFirstRow() == TableModelEvent.HEADER_ROW))
				splitTableColumns();
			refrechOrdinalData();
		}
	}
	
	/**
	 * Interni {@link JTable} objekat koji na svaki prosledjeni {@link TableModelEvent} 
	 * tipa TableModelEvent.HEADER_ROW objekat pokrece proces deljenja kolona. 
	 * Ovaj {@link JTable} objekat nudi mogucnost grupisanja Header-a tabele.
	 * @author Borko Grecic
	 */
	private class GroupHeaderSplitTable extends JTableGH {
		
		/**
		 * Serial version UID
		 */
		private static final long serialVersionUID = -7541095658832711740L;

		/**
		 * Konstruktor {@link GroupableTableHeader} objekta
		 */
		public GroupHeaderSplitTable() {
			super();
		}

		/**
		 * Konstruktor {@link GroupableTableHeader} objekta
		 * @param tableModel {@link TableModel} objekat koji se prosledjuje {@link JTable} objektu
		 */
		public GroupHeaderSplitTable(T tableModel) {
			this.setModel(tableModel);
		}

		/*
		 * (non-Javadoc)
		 * @see javax.swing.JTable#tableChanged(javax.swing.event.TableModelEvent)
		 */
		@Override
		public void tableChanged(TableModelEvent e) {
			super.tableChanged(e);
			if(e == null || (e.getFirstRow() == TableModelEvent.HEADER_ROW))
				splitTableColumns();
			refrechOrdinalData();
		}
	}
}