package es.ucm.fdi.tp.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import es.ucm.fdi.tp.base.model.GameAction;
import es.ucm.fdi.tp.base.model.GameState;
import es.ucm.fdi.tp.chess.ChessBoard;
import es.ucm.fdi.tp.extra.jboard.JBoard;
import es.ucm.fdi.tp.extra.jboard.JBoard.Shape;
import es.ucm.fdi.tp.extra.jcolor.ColorChooser;

public abstract class ReetBoardGameView <S extends GameState<S, A>, A extends GameAction<S, A>> extends GameView<S,A> {

	private static final long serialVersionUID = 1L;
	protected JPanel textArea;
	protected JPanel infoPlayers;
	protected JPanel eastPanel;
	protected JScrollPane scroll;
	protected JTextArea texto;
	protected Map<Integer, Color> colors;
	protected ColorChooser colorChooser;
	protected Color color1;
	protected Color color2;
	protected GameWindow<S,A> gCtrl;
	protected S state;
	
	protected abstract int getNumCols();
	protected abstract int getNumRows();
	protected abstract void mouseClicked(int row, int col, int clickCount, int mouseButton);
	//protected abstract Integer getPosition(int row, int col);
	protected abstract void keyTyped(int keyCode);
	//protected abstract Color getBackGround(int row, int col);
	protected abstract Shape getShape(int player);
	protected abstract Color getColor(int player);
	protected abstract Integer getPosition(int row, int col);
	
	private Object [][] data = {{new Integer(0) , "      "},
			{new Integer(1), "      "}};
	private String[] colNames = {"Player", "Color"};
	
	private JBoard board;
	//private int numOfCols;
	//private int numOfRows;
	//private int dim;
	
	public ReetBoardGameView(){
		initGUI();
		
	}
	
	private void initGUI(){
		
		texto = new JTextArea(17, 15);
		scroll = new JScrollPane(texto, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
		ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scroll.setPreferredSize(new Dimension(180, 200 ));
		texto.setEditable(false);
		texto.setLineWrap(true);
		texto.setWrapStyleWord(true);
		textArea = new JPanel();
		infoPlayers = new JPanel();
		eastPanel = new JPanel();
		eastPanel.setLayout(new BorderLayout());
		eastPanel.setPreferredSize(new Dimension(200, 400));
		textArea.setBorder(BorderFactory.createTitledBorder("Status Messages: "));
		textArea.add(scroll);
		eastPanel.add(textArea, BorderLayout.CENTER);
		initGUI2();
	}
	
	private void initGUI2(){
		colors = new HashMap<>();
		colorChooser = new ColorChooser(new JFrame(), "Choose Line Color", Color.BLACK);
		
		JTable table = new JTable(data, colNames) {
			private static final long serialVersionUID = 1L;

			// THIS IS HOW WE CHANGE THE COLOR OF EACH ROW
			@Override
			public Component prepareRenderer(TableCellRenderer renderer, int row, int col) {
				Component comp = super.prepareRenderer(renderer, row, col);

				// the color of row 'row' is taken from the colors table, if
				// 'null' setBackground will use the parent component color.
				
				if (col == 1){
					if(colors.get(row) != null){
						comp.setBackground(colors.get(row));
					}
					else{
						if(row == 0){
							comp.setBackground(color1);
						}
						else{
							comp.setBackground(color2);
						}
					}
				}
				else
					comp.setBackground(Color.WHITE);
				comp.setForeground(Color.BLACK);
				return comp;
			}
		};

		table.setToolTipText("Click on a row to change the color of a player");
		table.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				int row = table.rowAtPoint(evt.getPoint());
				int col = table.columnAtPoint(evt.getPoint());
				if (row >= 0 && col >= 0) {
					changeColor(row);
				}
			}

		});
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		for (int i = 0; i < table.getColumnCount(); i++) { 
			TableColumn col = table.getColumnModel().getColumn(i);
			col.setPreferredWidth(88); }
		
		infoPlayers.setBorder(BorderFactory.createTitledBorder("Player Information: "));
		JScrollPane infoColor = new JScrollPane(table);
		infoColor.setPreferredSize(new Dimension (180, 89));
		infoPlayers.add(infoColor);
		eastPanel.add(infoPlayers, BorderLayout.SOUTH);
		initGUI3();
	}
	
	private void initGUI3(){
		
		this.board = new JBoard() {

			private static final long serialVersionUID = 1L;

			@Override
			protected void mouseClicked(int row, int col, int clickCount, int mouseButton) {
				ReetBoardGameView.this.mouseClicked(row, col, clickCount, mouseButton);
			}

			@Override
			protected void keyTyped(int keyCode) {
				ReetBoardGameView.this.keyTyped(keyCode);
			}
			
			//Al final esta tambien sera abstarcta porque creo que voy acrear 
			//otro valor para el enumerado. CIRCLECHESS;
			@Override
			protected Shape getShape(int player) {
				return ReetBoardGameView.this.getShape(player);
			}

			@Override
			protected Integer getPosition(int row, int col) {
				return ReetBoardGameView.this.getPosition(row, col);
			}

			@Override
			protected int getNumRows() {
				return ReetBoardGameView.this.getNumRows();
			}

			@Override
			protected int getNumCols() {
				return ReetBoardGameView.this.getNumCols();
			}

			@Override
			protected Color getColor(int player) {
				return ReetBoardGameView.this.getColor(player);
			}

			@Override
			protected Color getBackground(int row, int col) {
				//return Color.LIGHT_GRAY;

				// use this for 2 chess like board
				 return (row+col) % 2 == 0 ? Color.LIGHT_GRAY : Color.BLACK;
			}

			@Override
			protected int getSepPixels() {
				return 1; // put to 0 if you don't want a separator between
							// cells
			}
			
			//Proporciona al Jboard el nombre de la ficha y en funcion de el 
			//Pone una imagen u otra
			@Override
			protected String setNombreFicha(int row, int col) {
				String nombre;
				byte p = (byte)state.at(row, col);
				nombre = ChessBoard.Piece.iconName(p);
				return nombre;
			}
				
		};
		
		this.add(board, BorderLayout.CENTER);
		this.add(eastPanel, BorderLayout.EAST);
	}

	private void changeColor(int row){
		colorChooser.setSelectedColorDialog(colors.get(row));
		colorChooser.openDialog();
		if (colorChooser.getColor() != null) {
			colors.put(row, colorChooser.getColor());
			repaint();
			if(row == 0){
				color1 = colorChooser.getColor();
			}
			else{
				color2 = colorChooser.getColor();
			}
		}
	}
	
	@Override
	public void showInfoMessage(String message){
		
		texto.append("*" + message + "\n");
	}
	
	@Override
	public void setState(S state){
		this.state = state;
	}
	
	@Override
	public void setGameViewCtrl(GameWindow<S,A> gCtrl){
		this.gCtrl = gCtrl;
	};
	
	@Override
	public void updateView(S state){
		this.state = state;
		this.board.repaint();
	}
	
	@Override
	public void enable(){
		gCtrl.setEnabled(true);
	}
	
	@Override
	public void disable(){
		gCtrl.setEnabled(false);
	}
	
	@Override
	public void deleteText(){
		texto.setText(null);
		
	}	
}
